package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Permission;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PermissionRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String PERMISSION_ID_KEY = "id";
    private static final String PERMISSION_ID_BINDING_KEY = ":" + PERMISSION_ID_KEY;
    private static final String PERMISSION_NAME_KEY = "name";
    private static final String PERMISSION_NAME_BINDING_KEY = ":" + PERMISSION_NAME_KEY;
    private static final String PERMISSION_CREATED_ON_KEY = "created_on";
    private static final String PERMISSION_CREATED_ON_BINDING_KEY = ":" + PERMISSION_CREATED_ON_KEY;
    private static final String PERMISSION_MODIFIED_ON_KEY = "modified_on";
    private static final String PERMISSION_MODIFIED_ON_BINDING_KEY = ":" + PERMISSION_MODIFIED_ON_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();

    static{
        primaryKeys.add(PERMISSION_ID_KEY);
        primaryKeys.add(PERMISSION_ID_BINDING_KEY);
    }

    private static final String SELECT_PERMISSION_BY_NAME = "SELECT BIN_TO_UUID(id, 1) as id, " +
            "name, " +
            "created_on, " +
            "modified_on " +
            "FROM Permission " +
            "WHERE name = " + PERMISSION_NAME_BINDING_KEY;


    public PermissionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Permission getPermissionByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(PERMISSION_NAME_KEY, name);
        Permission permissionToReturn = null;
        try{
            permissionToReturn = namedParameterJdbcTemplate.queryForObject(SELECT_PERMISSION_BY_NAME, parameterSource, new PermissionMapper());
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return permissionToReturn;
    }

    public Permission save(Permission permission){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PERMISSION_ID_KEY, PERMISSION_ID_BINDING_KEY, permission.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PERMISSION_NAME_KEY, PERMISSION_NAME_BINDING_KEY, permission.getName());
        if(permission.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PERMISSION_CREATED_ON_KEY, PERMISSION_CREATED_ON_BINDING_KEY,
                    Timestamp.from(permission.getCreatedOn().toInstant()));
        }
        if(permission.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PERMISSION_MODIFIED_ON_KEY, PERMISSION_MODIFIED_ON_BINDING_KEY,
                    Timestamp.from(permission.getCreatedOn().toInstant()));
        }
        String insertionValueString = columnNameToBindingValue.values().stream()
                .map(s -> {
                    if (primaryKeys.contains(s)){
                        return "UUID_TO_BIN(" + s + ", true)";
                    }
                    return s;
                }).collect(Collectors.joining(","));

        String onDuplicateKeyString = columnNameToBindingValue.entrySet().stream()
                .filter(es -> !primaryKeys.contains(es.getKey()))
                .map(es -> es.getKey() + " = " + es.getValue())
                .collect(Collectors.joining(","));

        StringBuilder recordSql = new StringBuilder("INSERT INTO Permission (")
                .append(String.join(",", sqlColumns))
                .append(") ")
                .append("VALUES (")
                .append(insertionValueString)
                .append(") ON DUPLICATE KEY UPDATE ")
                .append(onDuplicateKeyString);
        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
        return permission;
    }

    private void addSqlItem(List<String> columns, Map<String, String> sqlValues, MapSqlParameterSource bindingValues,
                            String columnName, String bindingKey, Object value){
        columns.add(columnName);
        sqlValues.put(columnName, bindingKey);
        bindingValues.addValue(columnName, value);
    }

    private static final class PermissionMapper implements RowMapper<Permission>{
        @Override
        public Permission mapRow(ResultSet rs, int i) throws SQLException {
            Permission permission = new Permission();
            permission.setId(getUUIDFromResultSet(rs, "id"));
            permission.setName(rs.getString("name"));
            permission.setCreatedOn(createZonedDateTime(rs,"created_on"));
            permission.setModifiedOn(createZonedDateTime(rs, "modified_on"));
            return permission;
        }
        private ZonedDateTime createZonedDateTime(ResultSet rs, String key) throws SQLException{
            Timestamp value = rs.getTimestamp(key);
            if(value != null){
                return ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
            }
            return null;
        }

        private UUID getUUIDFromResultSet(ResultSet rs, String key) throws SQLException{
            String uuid = rs.getString(key);
            if(uuid != null) {
                return UUID.fromString(uuid);
            }
            return null;
        }
    }
}
