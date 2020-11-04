package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String USER_ID_KEY = "id";
    private static final String USER_ID_BINDING_KEY = ":" + USER_ID_KEY;
    private static final String USERNAME_KEY = "username";
    private static final String USERNAME_BINDING_KEY = ":" + USERNAME_KEY;
    private static final String FIRST_NAME_KEY = "first_name";
    private static final String FIRST_NAME_BINDING_KEY = ":" + FIRST_NAME_KEY;
    private static final String LAST_NAME_KEY = "last_name";
    private static final String LAST_NAME_BINDING_KEY = ":" + LAST_NAME_KEY;
    private static final String PASSWORD_KEY = "password";
    private static final String PASSWORD_BINDING_KEY = ":" + PASSWORD_KEY;
    private static final String EMAIL_KEY = "email";
    private static final String EMAIL_BINDING_KEY = ":" + EMAIL_KEY;
    private static final String PHONE_NUMBER_KEY = "phone_number";
    private static final String PHONE_NUMBER_BINDING_KEY = ":" + PHONE_NUMBER_KEY;
    private static final String LOCKED_KEY = "locked";
    private static final String LOCKED_BINDING_KEY = ":" + LOCKED_KEY;
    private static final String ENABLED_KEY = "enabled";
    private static final String ENABLED_BINDING_KEY = ":" + ENABLED_KEY;
    private static final String CREDS_EXPIRE_ON_KEY = "creds_expire_on";
    private static final String CREDS_EXPIRE_ON_BINDING_KEY = ":" + CREDS_EXPIRE_ON_KEY;
    private static final String EXPIRED_ON_KEY = "expired_on";
    private static final String EXPIRED_ON_BINDING_KEY = ":" + EXPIRED_ON_KEY;
    private static final String CREATED_ON_KEY = "created_on";
    private static final String CREATED_ON_BINDING_KEY = ":" + CREATED_ON_KEY;
    private static final String MODIFIED_ON_KEY = "modified_on";
    private static final String MODIFIED_ON_BINDING_KEY = ":" + MODIFIED_ON_KEY;

    private static final String USER_ROLE_USER_ID_KEY = "user_id";
    private static final String USER_ROLE_USER_ID_BINDING_KEY = ":" + USER_ROLE_USER_ID_KEY;
    private static final String USER_ROLE_ROLE_ID_KEY = "role_id";
    private static final String USER_ROLE_ROLE_ID_BINDING_KEY = ":" + USER_ROLE_ROLE_ID_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();
    private static final List<String> user_role_primaryKeys = new ArrayList<>();

    static{
        primaryKeys.add(USER_ID_KEY);
        primaryKeys.add(USER_ID_BINDING_KEY);

        user_role_primaryKeys.add(USER_ROLE_USER_ID_KEY);
        user_role_primaryKeys.add(USER_ROLE_USER_ID_BINDING_KEY);
        user_role_primaryKeys.add(USER_ROLE_ROLE_ID_KEY);
        user_role_primaryKeys.add(USER_ROLE_ROLE_ID_BINDING_KEY);
    }

    private static final String SELECT_USER_W_ROLES = "SELECT BIN_TO_UUID(u.id, 1) as user_id," +
            "u.first_name, " +
            "u.last_name, " +
            "u.username, " +
            "u.password, " +
            "u.email, " +
            "u.phone_number, " +
            "u.created_on, " +
            "u.modified_on, " +
            "u.expired_on, " +
            "u.credentialsExpired_on, " +
            "u.locked, " +
            "u.enabled, " +
            "BIN_TO_UUID(r.id, 1) as role_id, " +
            "r.name as role_name, " +
            "BIN_TO_UUID(p.id, 1) as perm_id, " +
            "p.name as perm_name " +
            "FROM User u " +
            "LEFT JOIN User_Role ur ON u.id = ur.user_id " +
            "LEFT JOIN Role r on ur.role_id = r.id " +
            "LEFT JOIN Role_Permission rp ON r.id = rp.role_id " +
            "LEFT JOIN Permission p ON rp.permission_id = p.id " +
            "WHERE 1=1";

    private static final String WHERE_USERNAME_OR_EMAIL_EQUALS_USERNAME = "u.username = " + USERNAME_BINDING_KEY + " OR u.email = " + EMAIL_BINDING_KEY;
    private static final String UPDATE_USER_PASSWORD = "UPDATE User SET password = :password WHERE username = :username OR email = :email";

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public User save(User user){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, USER_ID_KEY, USER_ID_BINDING_KEY, user.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, FIRST_NAME_KEY, FIRST_NAME_BINDING_KEY, user.getFirstName());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, LAST_NAME_KEY, LAST_NAME_BINDING_KEY, user.getLastName());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, USERNAME_KEY, USERNAME_BINDING_KEY, user.getUsername());
        if(user.getPassword() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PASSWORD_KEY, PASSWORD_BINDING_KEY, user.getPassword());
        }
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, EMAIL_KEY, EMAIL_BINDING_KEY, user.getEmail());
        if(user.getPhoneNum() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, PHONE_NUMBER_KEY, PHONE_NUMBER_BINDING_KEY, user.getPhoneNum());
        }
        if(user.getCreatedOn() != null) {
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREATED_ON_KEY, CREATED_ON_BINDING_KEY, Timestamp.from(user.getCreatedOn().toInstant()));
        }
        if(user.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, MODIFIED_ON_KEY, MODIFIED_ON_BINDING_KEY, Timestamp.from(user.getModifiedOn().toInstant()));
        }
        if(user.getExpiredOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, EXPIRED_ON_KEY, EXPIRED_ON_BINDING_KEY, Timestamp.from(user.getCreatedOn().toInstant()));
        }
        if(user.getCredentialsExpiredOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREDS_EXPIRE_ON_KEY, CREDS_EXPIRE_ON_BINDING_KEY,
                    Timestamp.from(user.getCredentialsExpiredOn().toInstant()));
        }
        if(user.isLocked()) {
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, LOCKED_KEY, LOCKED_BINDING_KEY, user.isAccountNonLocked()
            );
        }
        if(user.isEnabled()){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ENABLED_KEY, ENABLED_BINDING_KEY, user.isEnabled());
        }

        StringBuilder recordSql = getSqlInsertStatement("User", primaryKeys, sqlColumns, columnNameToBindingValue, true);

        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
        return user;
    }

    public User addUserRoles(User user, Role role){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, USER_ROLE_USER_ID_KEY, USER_ROLE_USER_ID_BINDING_KEY, user.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, USER_ROLE_ROLE_ID_KEY, USER_ROLE_ROLE_ID_BINDING_KEY, role.getId().toString());

        StringBuilder recordSql = getSqlInsertStatement("User_Role", user_role_primaryKeys, sqlColumns, columnNameToBindingValue, false);
        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
        return user;
    }

    public User getUserByUserName(String username){
        String sql = SELECT_USER_W_ROLES + " AND " + WHERE_USERNAME_OR_EMAIL_EQUALS_USERNAME;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(USERNAME_KEY, username);
        parameterSource.addValue(EMAIL_KEY, username);
        UserRowCallBackHandler callBackHandler = new UserRowCallBackHandler();
        namedParameterJdbcTemplate.query(sql, parameterSource, callBackHandler);
        return callBackHandler.getUser();
    }

    public void updateUserPassword(String username, String password){
        String sql = UPDATE_USER_PASSWORD;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(PASSWORD_KEY, password)
                .addValue(USERNAME_KEY, username)
                .addValue(EMAIL_KEY, username);
        namedParameterJdbcTemplate.update(sql, parameterSource);
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
            insertStatement = new StringBuilder("INSERT INTO " + table + " (")
                    .append(String.join(",", sqlCols))
                    .append(") ")
                    .append("VALUES (")
                    .append(insertionValue)
                    .append(") ON DUPLICATE KEY UPDATE ")
                    .append(onDuplicateKeyString);
        }
        else{
            insertStatement = new StringBuilder("INSERT INTO " + table +" (")
                    .append(String.join(",", sqlCols))
                    .append(") ")
                    .append("VALUES (")
                    .append(insertionValue)
                    .append(") ON DUPLICATE KEY UPDATE user_id = UUID_TO_BIN(:user_id,1), role_id = UUID_TO_BIN(:role_id,1)");
        }
        return insertStatement;
    }

}
