package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Address;
import com.group1_cms.cms_antiques.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class AddressRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String ADDRESS_ID_KEY = "id";
    private static final String ADDRESS_ID_BINDING_KEY = ":" + ADDRESS_ID_KEY;
    private static final String CITY_ID_KEY = "city_id";
    private static final String CITY_ID_BINDING_KEY = ":" + CITY_ID_KEY;
    private static final String STREET_ADDRESS_KEY = "street_address";
    private static final String STREET_ADDRESS_BINDING_KEY = ":" + STREET_ADDRESS_KEY;
    private static final String STREET_ADDRESS_LINE2_KEY = "street_address_line_2";
    private static final String STREET_ADDRESS_LINE2_BINDING_KEY = ":" + STREET_ADDRESS_LINE2_KEY;
    private static final String CREATED_ON_KEY = "created_on";
    private static final String CREATED_ON_BINDING_KEY = ":" + CREATED_ON_KEY;
    private static final String MODIFIED_ON_KEY = "modified_on";
    private static final String MODIFIED_ON_BINDING_KEY = ":" + MODIFIED_ON_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();
    private static final List<String> foreignKeys = new ArrayList<>();

    static{
        primaryKeys.add(ADDRESS_ID_KEY);
        primaryKeys.add(ADDRESS_ID_BINDING_KEY);

        foreignKeys.add(CITY_ID_KEY);
        foreignKeys.add(CITY_ID_BINDING_KEY);
    }

    private static final String SELECT_ADDRESS_WITH_CITY_BY_STREET_ADDRESS_AND_CITYID = "SELECT BIN_TO_UUID(a.id, 1) as address_id, " +
        "a.street_address, " +
        "a.street_address_line_2, " +
        "BIN_TO_UUID(c.id, 1) as city_id, " +
        "c.postal_code " +
        "FROM Address a  " +
        "LEFT JOIN City c on a.city_id = c.id " +
        "WHERE a.street_address = " + STREET_ADDRESS_BINDING_KEY + " AND a.city_id = UUID_TO_BIN(" + CITY_ID_BINDING_KEY + ", 1)";

    private static final String DELETE_ADDRESS_FROM_DB_BY_ID = "DELETE FROM Address WHERE " + ADDRESS_ID_KEY + " = " + ADDRESS_ID_BINDING_KEY;

    @Autowired
    public AddressRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Address getAddressByStreetAddressAndCity(String streetAddress, City city){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(STREET_ADDRESS_KEY, streetAddress);
        parameterSource.addValue(CITY_ID_KEY, city.getId().toString());
        AddressRowMapper addressRowMapper = new AddressRowMapper();
        Address addressToReturn = null;
        try{
            addressToReturn = namedParameterJdbcTemplate.queryForObject(SELECT_ADDRESS_WITH_CITY_BY_STREET_ADDRESS_AND_CITYID, parameterSource, addressRowMapper);
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
        return addressToReturn;
    }

    public Address save(Address address){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, ADDRESS_ID_KEY, ADDRESS_ID_BINDING_KEY, address.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, STREET_ADDRESS_KEY, STREET_ADDRESS_BINDING_KEY, address.getStreetAddress());
        if(address.getStreetAddressLine2() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, STREET_ADDRESS_LINE2_KEY, STREET_ADDRESS_LINE2_BINDING_KEY, address.getStreetAddressLine2());
        }
        if(address.getCity().getId() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CITY_ID_KEY, CITY_ID_BINDING_KEY, address.getCity().getId().toString());
        }
        if(address.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREATED_ON_KEY, CREATED_ON_BINDING_KEY, Timestamp.from(address.getModifiedOn().toInstant()));
        }
        if(address.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, MODIFIED_ON_KEY, MODIFIED_ON_BINDING_KEY, Timestamp.from(address.getModifiedOn().toInstant()));
        }

        StringBuilder recordSql = getSqlInsertStatement("Address", primaryKeys, foreignKeys, sqlColumns, columnNameToBindingValue);

        namedParameterJdbcTemplate.update(recordSql.toString(), bindingValues);

        return address;
    }

    public int deleteAddress(Address address){
        String sql = DELETE_ADDRESS_FROM_DB_BY_ID;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(ADDRESS_ID_KEY, address.getId().toString());
        return namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
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

    private static final class AddressRowMapper implements RowMapper<Address> {

        @Override
        public Address mapRow(ResultSet rs, int i) throws SQLException {
            Address addresss = new Address();
            UUID addressId = getUUIDFromResultSet(rs, "address_id");
            String streetAddress = rs.getString("street_address");
            String streetAddressLine2 = rs.getString("street_address_line_2");
            UUID cityId = getUUIDFromResultSet(rs, "city_id");
            String postalCode = rs.getString("postal_code");
            if(addressId != null){
                addresss.setId(addressId);
            }
            if(streetAddress != null){
                addresss.setStreetAddress(streetAddress);
            }
            if(streetAddressLine2 != null){
                addresss.setStreetAddressLine2(streetAddressLine2);
            }
            return addresss;
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
