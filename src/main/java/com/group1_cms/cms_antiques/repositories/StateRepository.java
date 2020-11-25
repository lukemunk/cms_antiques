package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.State;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    private static final String SELECT_ALL_STATES = "SELECT BIN_TO_UUID(id, 1)as state_id, " +
        "name as state_name " +
        "FROM State " +
        "ORDER BY state_name";

    private static final String SELECT_STATE_WITH_CITIES = "SELECT BIN_TO_UUID(s.id , 1) as state_id, " +
        "s.name as state_name, " +
        "BIN_TO_UUID(c.id, 1) as city_id, " +
        "c.name as city_name " +
        "FROM State s LEFT JOIN " +
        "City c ON s.id = c.state_id";

    private static final String SELECT_STATE_BY_NAME = "SELECT BIN_TO_UUID(id , 1) as state_id, " +
        "name as state_name " +
        "FROM State " +
        "WHERE name = " + NAME_BINDING_KEY;

    private static final String WHERE_NAME_EQUALS_NAME = " WHERE state_name = " + NAME_BINDING_KEY;


    @Autowired
    public StateRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<State> getAllStates(){
        List<State> stateList = null;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        StateRowMapper stateRowMapper = new StateRowMapper();
        String sql = SELECT_ALL_STATES;
        try{
            stateList = namedParameterJdbcTemplate.query(sql, parameterSource, stateRowMapper);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return stateList;
    }

    public State getStateWithCitiesByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        StateCityCallBackHandler stateCityCallBackHandler = new StateCityCallBackHandler();
        String sql = SELECT_STATE_WITH_CITIES + WHERE_NAME_EQUALS_NAME;
        State stateToReturn = null;
        parameterSource.addValue(NAME_KEY, name);
        try{
            namedParameterJdbcTemplate.query(sql, parameterSource, stateCityCallBackHandler);
            stateToReturn = stateCityCallBackHandler.getState();
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }

        return stateToReturn;
    }

    public List<State> getAllStatesWithCities(){
        StateCityCallBackHandler stateCityCallBackHandler = new StateCityCallBackHandler();
        List<State> stateList = null;
        try{
            namedParameterJdbcTemplate.query(SELECT_STATE_WITH_CITIES, stateCityCallBackHandler);
            stateList = stateCityCallBackHandler.getStateList();
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return stateList;
    }

    public State getStateByName(String name){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(NAME_KEY, name);
        StateRowMapper stateRowMapper = new StateRowMapper();
        State stateToReturn = null;
        try{
            stateToReturn = namedParameterJdbcTemplate.queryForObject(SELECT_STATE_BY_NAME, parameterSource, stateRowMapper);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return stateToReturn;
    }


    public State save(State state){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, STATE_ID_KEY, STATE_ID_BINDING_KEY, state.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, NAME_KEY, NAME_BINDING_KEY, state.getName());
        if(state.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREATED_ON_KEY, CREATED_ON_BINDING_KEY, Timestamp.from(state.getCreatedOn().toInstant()));
        }
        if(state.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, MODIFIED_ON_KEY, MODIFIED_ON_BINDING_KEY, Timestamp.from(state.getModifiedOn().toInstant()));
        }

        StringBuilder recordSql = getSqlInsertStatement("State", primaryKeys, sqlColumns, columnNameToBindingValue);

        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);

        return state;
    }

    private void addSqlItem(List<String> columns, Map<String, String> sqlValues, MapSqlParameterSource bindingValues,
                            String columnName, String bindingKey, Object value){
        columns.add(columnName);
        sqlValues.put(columnName, bindingKey);
        bindingValues.addValue(columnName, value);
    }

    private StringBuilder getSqlInsertStatement(String table, List<String> keys, List<String> sqlCols, Map<String, String> bindingValues)
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

        insertStatement = new StringBuilder("INSERT INTO " + table + " (")
                .append(String.join(",", sqlCols))
                .append(") ")
                .append("VALUES (")
                .append(insertionValue)
                .append(") ON DUPLICATE KEY UPDATE ")
                .append(onDuplicateKeyString);
        return insertStatement;
    }

    private static final class StateCityCallBackHandler implements RowCallbackHandler {
        private State state;
        private List<State> stateList = new ArrayList<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {

            if(state == null || state.getId() != getUUIDFromResultSet(rs, "id")){
                state = new State();
                stateList.add(state);
            }
            if(state.getId() == null){
                state.setId(getUUIDFromResultSet(rs, "id"));
            }
            if(state.getName() == null){
                state.setName(rs.getString("name"));
            }
            UUID cityId = getUUIDFromResultSet(rs, "city_id");

            if(cityId != null){
                City currentCity = new City();
                currentCity.setId(cityId);
                currentCity.setName(rs.getString("city_name"));
                state.getCities().put(cityId, currentCity);
            }
        }

        public State getState(){
            if(state == null){
                state = new State();
            }
            return state;
        }

        public List<State> getStateList(){
            return stateList;
        }

        private UUID getUUIDFromResultSet(ResultSet rs, String key) throws SQLException{
            String uuid = rs.getString(key);
            if(uuid != null) {
                return UUID.fromString(uuid);
            }
            return null;
        }
    }

    private static final class StateRowMapper implements RowMapper<State>{

        @Override
        public State mapRow(ResultSet rs, int i) throws SQLException {
            State state = new State();
            UUID stateId = getUUIDFromResultSet(rs, "state_id");
            String stateName = rs.getString("state_name");
            if(stateId != null){
                state.setId(stateId);
            }
            if(stateName != null){
                state.setName(stateName);
            }
            return state;
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
