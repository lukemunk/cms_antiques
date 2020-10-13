package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class UserRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_USER_W_ROLES = "SELECT BIN TO UUID(u.id) as user_id," +
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
            "r.id as role_id, " +
            "r.name as role_name, " +
            "p.id as perm_id, " +
            "p.name as perm_name " +
            "FROM User u " +
            "LEFT JOIN User_Role ur ON u.id = ur.user_id " +
            "LEFT JOIN Role r on ur.role_id = r.id " +
            "LEFT JOIN Role_Permission rp ON r.id = rp.role_id " +
            "LEFT JOIN PERMISSION p ON rp.permission_id = p.id " +
            "WHERE 1=1";

    private static final String SAVE_USER = "INSERT INTO User (" +
            "first_name, " +
            "last_name, " +   // :id :username
            "username, " +
            "password, " +
            "email, " +
            "phone_number, " +
            "created_on, " +
            "modified_on, " +
            "expired_on, " +
            "credentialsExpired_on, " +
            "locked, " +
            "enabled, " +
            ")";

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    public User getUserByUserName(String username){
        String sql = SELECT_USER_W_ROLES + " AND WHERE u.username = :username OR u.email = :email";
        return null; //TODO add logic for getting a user from database
    }
}
