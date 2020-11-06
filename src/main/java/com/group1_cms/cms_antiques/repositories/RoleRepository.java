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

public class RoleRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String ROLE_ID_KEY = "id";
    private static final String ROLE_ID_BINDING_KEY = ":" + ROLE_ID_KEY;
    private static final String ROLE_NAME_KEY = "name";
    private static final String ROLE_NAME_BINDING_KEY = ":" + ROLE_NAME_KEY;
    private static final String ROLE_CREATED_ON_KEY = "created_on";
    private static final String ROLE_CREATED_BINDING_KEY = ":" + ROLE_CREATED_ON_KEY;
    private static final String ROLE_MODIFIED_ON_KEY = "modified_on";
    private static final String ROLE_MODIFIED_ON_BINDING_KEY = ":" + ROLE_MODIFIED_ON_KEY;

    /**
     * Strings for Role_Permission table
     */
    private static final String ROLE_PERMISSIONS_ROLE_ID_KEY = "role_id";
    private static final String ROLE_PERMISSIONS_ROLE_ID_KEY_BINDING = ":" + ROLE_PERMISSIONS_ROLE_ID_KEY;
    private static final String ROLE_PERMISSIONS_PERMISSION_ID_KEY = "permission_id";
    private static final String ROLE_PERMISSIONS_PERMISSION_ID_BINDING_KEY = ":" + ROLE_PERMISSIONS_PERMISSION_ID_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();
    private static final List<String> role_permission_primaryKeys = new ArrayList<>();

    static{
        primaryKeys.add(ROLE_ID_KEY);
        primaryKeys.add(ROLE_ID_BINDING_KEY);

        role_permission_primaryKeys.add(ROLE_PERMISSIONS_ROLE_ID_KEY);
        role_permission_primaryKeys.add(ROLE_PERMISSIONS_ROLE_ID_KEY_BINDING);
        role_permission_primaryKeys.add(ROLE_PERMISSIONS_PERMISSION_ID_KEY);
        role_permission_primaryKeys.add(ROLE_PERMISSIONS_PERMISSION_ID_BINDING_KEY);
    }

    private static final String SELECT_ROLE_BY_NAME = "SELECT BIN_TO_UUID(id, 1) as id," +
            "name, " +
            "created_on, " +
            "modified_on " +
            "FROM Role " +
            "WHERE name = " + ROLE_NAME_BINDING_KEY;

    private static final String SELECT_ROLE_WITH_PERMISSIONS = "SELECT BIN_TO_UUID(r.id, 1) as id," +
            "r.name as role_name, " +
            "BIN_TO_UUID(p.id, 1) as permission_id, " +
            "p.name as permission_name " +
            "FROM Role r " +
            "LEFT JOIN Role_Permission rp on r.id = rp.role_id " +
            "LEFT JOIN Permission p on rp.permission_id = p.id " +
            "WHERE r.name = " + ROLE_NAME_BINDING_KEY;

    public RoleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Role getRoleByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ROLE_NAME_KEY, name);
        Role roleToReturn = null;
        try{
            roleToReturn = namedParameterJdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME, parameterSource, new RoleMapper());
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }

        return roleToReturn;
    }

    public Role getRoleWithPermissionsByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        RolePermissionsCallBackHandler roleWithPermissionCallBackHandler = new RolePermissionsCallBackHandler();
        parameterSource.addValue(ROLE_NAME_KEY, name);
        Role roleToReturn = null;
        try{
            namedParameterJdbcTemplate.query(SELECT_ROLE_WITH_PERMISSIONS, parameterSource, roleWithPermissionCallBackHandler);
            roleToReturn = roleWithPermissionCallBackHandler.getRole();
        }
        catch(EmptyResultDataAccessException ex){
            return null;
        }

        return roleToReturn;
    }
    
    public Role save(Role role){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_ID_KEY, ROLE_ID_BINDING_KEY, role.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_NAME_KEY, ROLE_NAME_BINDING_KEY, role.getName());
        if(role.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_CREATED_ON_KEY, ROLE_CREATED_BINDING_KEY, Timestamp.from(role.getCreatedOn().toInstant()));
        }
        if(role.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_MODIFIED_ON_KEY, ROLE_MODIFIED_ON_BINDING_KEY, Timestamp.from(role.getModifiedOn().toInstant()));
        }

        StringBuilder recordSql = getSqlInsertStatement("Role", primaryKeys, sqlColumns, columnNameToBindingValue, true);
        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
        return role;
    }

    public void addPermissions(Role role, Permission permission){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_PERMISSIONS_ROLE_ID_KEY, ROLE_PERMISSIONS_ROLE_ID_KEY_BINDING, role.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ROLE_PERMISSIONS_PERMISSION_ID_KEY, ROLE_PERMISSIONS_PERMISSION_ID_BINDING_KEY, permission.getId().toString());

        StringBuilder recordSql = getSqlInsertStatement("Role_Permission", role_permission_primaryKeys, sqlColumns, columnNameToBindingValue, false);
        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
    }

    private void addSqlItem(List<String> columns, Map<String, String> sqlValues, MapSqlParameterSource bindingValues,
                            String columnName, String bindingKey, Object value){
        columns.add(columnName);
        sqlValues.put(columnName, bindingKey);
        bindingValues.addValue(columnName, value);
    }

    private StringBuilder getSqlInsertStatement(String table, List<String> keys, List<String> sqlCols, Map<String, String> bindingValues
            , boolean possibleDuplicates)
    {
        StringBuilder insertStatement;
        String insertionValue = bindingValues.values()
                .stream()
                .map(s -> {
                    if (keys.contains(s)){
                        return "UUID_TO_BIN(" + s + ", true)";
                    }
                    return s;
                }).collect(Collectors.joining(","));

        String onDuplicateKeyString = bindingValues.entrySet().stream()
                .filter(es -> !keys.contains(es.getKey()))
                .map(es -> es.getKey() + " = " + es.getValue())
                .collect(Collectors.joining(","));

        if(possibleDuplicates){
            insertStatement = new StringBuilder("INSERT INTO " + table +  " (")
                    .append(String.join(",", sqlCols))
                    .append(") ")
                    .append("VALUES (")
                    .append(insertionValue)
                    .append(") ON DUPLICATE KEY UPDATE ")
                    .append(onDuplicateKeyString);
        }
        else{
            insertStatement = new StringBuilder("INSERT INTO " + table + " (")
                    .append(String.join(",", sqlCols))
                    .append(") ")
                    .append("VALUES (")
                    .append(insertionValue)
                    .append(")");
        }
        return insertStatement;
    }

    private static final class RoleMapper implements RowMapper<Role>{

        @Override
        public Role mapRow(ResultSet rs, int i) throws SQLException {
            Role role = new Role();
            role.setId(getUUIDFromResultSet(rs, "id"));
            role.setName(rs.getString("name"));
            role.setCreatedOn(createZonedDateTime(rs,"created_on"));
            role.setModifiedOn(createZonedDateTime(rs, "modified_on"));
            return role;
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

    private final class RolePermissionsCallBackHandler implements RowCallbackHandler{
        private Role role;

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            if(role == null){
                role = new Role();
            }
            if(role.getId() == null){
                role.setId(getUUIDFromResultSet(rs, "id"));
            }
            if(role.getName() == null){
                role.setName(rs.getString("role_name"));
            }
            UUID permissionId = getUUIDFromResultSet(rs, "permission_id");
            Permission currentPermission = new Permission();
            if(permissionId != null){
                currentPermission = role.getPermissionById(permissionId);
            }
            if(currentPermission == null && permissionId != null){
                Permission newPermission = new Permission();
                newPermission.setId(permissionId);
                newPermission.setName(rs.getString("permission_name"));
                role.getPermissions().put(permissionId, newPermission);
            }
        }
        private UUID getUUIDFromResultSet(ResultSet rs, String key) throws SQLException{
            String uuid = rs.getString(key);
            if(uuid != null) {
                return UUID.fromString(uuid);
            }
            return null;
        }
        public Role getRole(){
           if(role == null){
               role = new Role();
           }
           return role;
        }
    }
}
