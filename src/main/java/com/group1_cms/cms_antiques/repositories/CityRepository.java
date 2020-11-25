package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Address;
import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.State;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class CityRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CITY_ID_KEY = "id";
    private static final String CITY_ID_BINDING_KEY = ":" + CITY_ID_KEY;
    private static final String STATE_ID_KEY = "state_id";
    private static final String STATE_ID_BINDING_KEY = ":" + STATE_ID_KEY;
    private static final String STATE_NAME_KEY = "state_name";
    private static final String STATE_NAME_BINDING_KEY = ":" + STATE_NAME_KEY;
    private static final String NAME_KEY = "name";
    private static final String NAME_BINDING_KEY = ":" + NAME_KEY;
    private static final String POSTALCODE_KEY = "postal_code";
    private static final String POSTALCODE_BINDING_KEY = ":" + POSTALCODE_KEY;
    private static final String CREATED_ON_KEY = "created_on";
    private static final String CREATED_ON_BINDING_KEY = ":" + CREATED_ON_KEY;
    private static final String MODIFIED_ON_KEY = "modified_on";
    private static final String MODIFIED_ON_BINDING_KEY = ":" + MODIFIED_ON_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();
    private static final List<String> foreignKeys = new ArrayList<>();

    static{
        primaryKeys.add(CITY_ID_KEY);
        primaryKeys.add(CITY_ID_BINDING_KEY);

        foreignKeys.add(STATE_ID_KEY);
        foreignKeys.add(STATE_ID_BINDING_KEY);
    }

    private static final String SELECT_CITY_WITH_ADDRESS_BY_ID = "SELECT BIN_TO_UUID(c.id, 1) as city_id, " +
            "c.name as city_name, " +
            "c.postal_code, " +
            "BIN_TO_UUID(a.id, 1) as address_id, " +
            "a.street_address " +
            "FROM City c " +
            "LEFT JOIN ";

    private static final String SELECT_CITY_WITH_STATE_BY_NAME_AND_STATE_AND_POSTAL_CODE = "SELECT BIN_TO_UUID(c.id, 1) as city_id, " +
            "c.name as city_name, " +
            "c.postal_code, " +
            "BIN_TO_UUID(s.id, 1) as state_id, " +
            "s.name as state_name " +
            "FROM City c " +
            "INNER JOIN State s ON c.state_id = s.id " +
            "WHERE c.name = " + NAME_BINDING_KEY + " AND s.name = " + STATE_NAME_BINDING_KEY + " AND c.postal_code = " + POSTALCODE_BINDING_KEY;

    public CityRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    public City getCityByNameAndStateAndPostalCode(String name, String stateName, String postalCode){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(NAME_KEY, name);
        parameterSource.addValue(POSTALCODE_KEY, postalCode);
        parameterSource.addValue("state_name", stateName);
        CityRowMapper cityRowMapper = new CityRowMapper();
        City cityToReturn = null;
        try{
            cityToReturn = namedParameterJdbcTemplate.queryForObject(SELECT_CITY_WITH_STATE_BY_NAME_AND_STATE_AND_POSTAL_CODE, parameterSource, cityRowMapper);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return cityToReturn;
    }

    public City save(City city){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CITY_ID_KEY, CITY_ID_BINDING_KEY, city.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, NAME_KEY, NAME_BINDING_KEY, city.getName());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, STATE_ID_KEY, STATE_ID_BINDING_KEY, city.getState().getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, POSTALCODE_KEY, POSTALCODE_BINDING_KEY, city.getPostalCode());
        if(city.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREATED_ON_KEY, CREATED_ON_BINDING_KEY, Timestamp.from(city.getCreatedOn().toInstant()));
        }
        if(city.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, MODIFIED_ON_KEY, MODIFIED_ON_BINDING_KEY, Timestamp.from(city.getModifiedOn().toInstant()));
        }

        StringBuilder recordSql = getSqlInsertStatement("City", primaryKeys, foreignKeys, sqlColumns, columnNameToBindingValue);

        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);
        return city;
    }


    private void addSqlItem(List<String> columns, Map<String, String> sqlValues, MapSqlParameterSource bindingValues,
                            String columnName, String bindingKey, Object value){
        columns.add(columnName);
        sqlValues.put(columnName, bindingKey);
        bindingValues.addValue(columnName, value);
    }

    private StringBuilder getSqlInsertStatement(String table, List<String> keys, List<String> sqlCols, Map<String, String> bindingValues){
        return getSqlInsertStatement(table, keys, null, sqlCols, bindingValues);
    }

    private StringBuilder getSqlInsertStatement(String table, List<String> keys, List<String> foreignKeys, List<String> sqlCols, Map<String, String> bindingValues)
    {
        StringBuilder insertStatement;
        String insertionValue = bindingValues.values()
                .stream()
                .map(s -> {
                    if (keys.contains(s) || (foreignKeys != null && foreignKeys.contains(s))){
                        return "UUID_TO_BIN(" + s + ", true)";
                    }
                    return s;
                }).collect(Collectors.joining(","));

        String onDuplicateKeyString = bindingValues.entrySet().stream()
                .filter(es -> !keys.contains(es.getKey()))
                .map(es -> {
                        if (foreignKeys.contains(es.getKey())){
                            return es.getKey() + " = UUID_TO_BIN(" + es.getValue() + ", true)";
                        }
                        return es.getKey() + " = " + es.getValue();
                }).collect(Collectors.joining(",")); //es.getKey() + " = " + es.getValue()

        insertStatement = new StringBuilder("INSERT INTO " + table + " (")
                .append(String.join(",", sqlCols))
                .append(") ")
                .append("VALUES (")
                .append(insertionValue)
                .append(") ON DUPLICATE KEY UPDATE ")
                .append(onDuplicateKeyString);
        return insertStatement;
    }

    private static final class CityAddressCallBackHandler implements RowCallbackHandler{

        private City city;
        private List<City> cityList = new ArrayList<>();
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            if(city == null || city.getId() != getUUIDFromResultSet(rs, "id")){
                city = new City();
                cityList.add(city);
            }
            if(city.getId() == null){
                city.setId(getUUIDFromResultSet(rs, "id"));
            }
            if(city.getName() == null){
                city.setName(rs.getString("name"));
            }
            if(city.getPostalCode() == null){
                city.setPostalCode(rs.getString("postal_code"));
            }
            UUID addressId = getUUIDFromResultSet(rs, "address_id");

            if(addressId == null){
                Address currentAddress = new Address();
                currentAddress.setId(addressId);
                currentAddress.setStreetAddress(rs.getString("street_address"));
                city.getAddressess().put(addressId, currentAddress);

            }
        }

        public City getCity(){
            if(city == null){
                city = new City();
            }
            return city;
        }

        public List<City> getCityList(){
            return cityList;
        }

        private UUID getUUIDFromResultSet(ResultSet rs, String key) throws SQLException{
            String uuid = rs.getString(key);
            if(uuid != null) {
                return UUID.fromString(uuid);
            }
            return null;
        }
    }

    private static final class CityRowMapper implements RowMapper<City>{

        @Override
        public City mapRow(ResultSet rs, int i) throws SQLException {
            City city = new City();
            UUID cityId = getUUIDFromResultSet(rs, "city_id");
            String cityName = rs.getString("city_name");
            UUID stateId = getUUIDFromResultSet(rs, "state_id");
            String postalCode = rs.getString("postal_code");
            if(cityId != null){
                city.setId(cityId);
            }
            if(cityName != null){
                city.setName(cityName);
            }
            if(postalCode != null){
                city.setPostalCode(postalCode);
            }
            if(stateId != null){
                State state = new State();
                state.setId(stateId);
                state.setName(rs.getString("state_name"));
                city.setState(state);
            }
            return city;
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
