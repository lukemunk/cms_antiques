package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StateRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String STATE_ID_KEY = "id";
    private static final String STATE_ID_BINDING_KEY = ":" + STATE_ID_KEY;
    private static final String NAME_KEY = "name";
    private static final String NAME_BINDING_KEY = ":" + NAME_KEY;
    private static final String CREATED_ON_KEY = "created_on";
    private static final String CREATED_ON_BINDING_KEY = ":" + CREATED_ON_KEY;
    private static final String MODIFIED_ON_KEY = "modified_on";
    private static final String MODIFIED_ON_BINDING_KEY = ":" + MODIFIED_ON_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();

    static{
        primaryKeys.add(STATE_ID_KEY);
        primaryKeys.add(STATE_ID_BINDING_KEY);
    }

    private static final String SELECT_STATE_BY_ID = "SELECT BIN_TO_UUID(id , 1), " +
    "name, " +
    "created_on, " +
    "modified_on " +
    "FROM State WHERE BIN_TO_UUID(" + STATE_ID_KEY + ", 1) = " + STATE_ID_BINDING_KEY;

    private static final String SELECT_ALL_STATES = "SELECT BIN_TO_UUID(id , 1), " +
            "name, " +
            "created_on, " +
            "modified_on " +
            "FROM State";

    @Autowired
    public StateRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public State getStateById(String id){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(STATE_ID_KEY, id);
        return null;
    }

    private static final class StateMapper implements RowMapper<State>{

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {
            State state = new State();
            state.setId(getUUIDFromResultSet(rs, "id"));
            state.setName(rs.getString("name"));
            return null; //TODO EDIT

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
