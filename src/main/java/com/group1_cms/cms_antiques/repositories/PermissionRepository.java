package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    private static final String SELECT_PERMISSION_WITH_ROLES = "SELECT BIN_TO_UUID(p.id, 1) as id, " +
        "p.name as permission_name, " +
        "p.created_on, " +
        "p.modified_on, " +
        "BIN_TO_UUID(r.id, 1) as role_id, " +
        "r.name as role_name " +
        "FROM Permission p " +
        "LEFT JOIN Role_Permission rp ON p.id = rp.permission_id " +
        "LEFT JOIN Role r ON rp.role_id = r.id " +
        "WHERE p.id = UUID_TO_BIN(" + PERMISSION_ID_BINDING_KEY + ", 1)";

    private static final String SELECT_ALL_PERMISSIONS = "SELECT BIN_TO_UUID(id, 1) as id, " +
            "name, " +
            "created_on, " +
            "modified_on " +
            "FROM Permission";

    private static final String DELETE_PERMISSION_BY_ID = "DELETE FROM Permission " +
            "WHERE id = UUID_TO_BIN(" + PERMISSION_ID_BINDING_KEY + ", 1)";

    private static final String REMOVE_ROLE_PERMISSIONS = "DELETE FROM Role_Permission " +
            "WHERE permission_id = UUID_TO_BIN(:permission_id, 1)";


    public PermissionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Permission getPermissionByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(PERMISSION_NAME_KEY, name);
        try{
            return namedParameterJdbcTemplate.queryForObject(SELECT_PERMISSION_BY_NAME, parameterSource, new PermissionRowMapper());
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    public Permission getPermissionById(String id){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        PermissionsRoleCallBackHandler callBackHandler = new PermissionsRoleCallBackHandler();
        parameterSource.addValue(PERMISSION_ID_KEY, id);
        try{
            namedParameterJdbcTemplate.query(SELECT_PERMISSION_WITH_ROLES, parameterSource, callBackHandler);
            return callBackHandler.getPermission();
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Permission> getAllPermissions(){
        PermissionRowMapper permissionRowMapper = new PermissionRowMapper();
        try{
            return namedParameterJdbcTemplate.query(SELECT_ALL_PERMISSIONS, permissionRowMapper);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
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

    public void addToRole_Permission(String permission_id, String role_id){
        String sql = "INSERT INTO Role_Permission(permission_id, role_id) " +
                "VALUES (UUID_TO_BIN(:permission_id, 1), UUID_TO_BIN(:role_id, 1))";
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        bindingValues.addValue("permission_id", permission_id);
        bindingValues.addValue("role_id", role_id);
        namedParameterJdbcTemplate.update(sql, bindingValues);

    }

    public int deletePermissionById(String permission_id){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(PERMISSION_ID_KEY, permission_id);
        return namedParameterJdbcTemplate.update(DELETE_PERMISSION_BY_ID, parameterSource);
    }

    public void deleteRole_Permission(String permission_id){
        MapSqlParameterSource bindingValues= new MapSqlParameterSource();
        bindingValues.addValue("permission_id", permission_id);
        namedParameterJdbcTemplate.update(REMOVE_ROLE_PERMISSIONS, bindingValues);
    }

    private void addSqlItem(List<String> columns, Map<String, String> sqlValues, MapSqlParameterSource bindingValues,
                            String columnName, String bindingKey, Object value){
        columns.add(columnName);
        sqlValues.put(columnName, bindingKey);
        bindingValues.addValue(columnName, value);
    }

    private static final class PermissionRowMapper implements RowMapper<Permission>{
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

    private final class PermissionsRoleCallBackHandler implements RowCallbackHandler {
        private Permission permission;

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            if(permission == null){
                permission = new Permission();
            }
            if(permission.getId() == null){
                permission.setId(getUUIDFromResultSet(rs, "id"));
            }
            if(permission.getName() == null){
                permission.setName(rs.getString("permission_name"));
            }
            if(permission.getCreatedOn() == null){
                permission.setCreatedOn(createZonedDateTime(rs, "created_on"));
            }
            if(permission.getModifiedOn() == null){
                permission.setModifiedOn(createZonedDateTime(rs, "modified_on"));
            }
            UUID roleId = getUUIDFromResultSet(rs, "role_id");
            Role currentRole = new Role();

            if(roleId != null){
                currentRole = permission.getRoleById(roleId);
            }
            if(currentRole == null && roleId != null){
                Role newRole = new Role();
                newRole.setId(roleId);
                newRole.setName(rs.getString("role_name"));
                permission.getRoles().put(roleId, newRole);
            }
        }
        private UUID getUUIDFromResultSet(ResultSet rs, String key) throws SQLException{
            String uuid = rs.getString(key);
            if(uuid != null) {
                return UUID.fromString(uuid);
            }
            return null;
        }

        private ZonedDateTime createZonedDateTime(ResultSet rs, String key) throws SQLException{
            Timestamp value = rs.getTimestamp(key);
            if(value != null){
                return ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
            }
            return null;
        }
        public Permission getPermission(){
            if(permission == null){
                permission = new Permission();
            }
            return permission;
        }
    }
}
