package com.group1_cms.cms_antiques.repositories;

import com.group1_cms.cms_antiques.models.Category;
import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.State;
import org.springframework.beans.factory.annotation.Autowired;
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

public class CategoryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CATEGORY_ID_KEY = "id";
    private static final String CATEGORY_ID_BINDING_KEY = ":" + CATEGORY_ID_KEY;
    private static final String NAME_KEY = "name";
    private static final String NAME_BINDING_KEY = ":" + NAME_KEY;
    private static final String CREATED_ON_KEY = "created_on";
    private static final String CREATED_ON_BINDING_KEY = ":" + CREATED_ON_KEY;
    private static final String MODIFIED_ON_KEY = "modified_on";
    private static final String MODIFIED_ON_BINDING_KEY = ":" + MODIFIED_ON_KEY;

    private static final List<String> primaryKeys = new ArrayList<>();

    static{
        primaryKeys.add(CATEGORY_ID_KEY);
        primaryKeys.add(CATEGORY_ID_BINDING_KEY);
    }

    private static final String SELECT_ALL_CATEGORIES = "SELECT BIN_TO_UUID(id, 1) as id, " +
        "name, " +
        "created_on, " +
        "modified_on " +
        "FROM Category";

    private static final String SELECT_CATEGORY_BY_ID = "SELECT BIN_TO_UUID(id, 1) as id, " +
        "name, " +
        "created_on, " +
        "modified_on " +
        "FROM Category " +
        "WHERE id = UUID_TO_BIN(" + CATEGORY_ID_BINDING_KEY + ", 1);";

    private static final String DELETE_CATEGORY_BY_ID = "DELETE FROM Category " +
            "WHERE id = UUID_TO_BIN(" + CATEGORY_ID_BINDING_KEY + ", 1)";


    @Autowired
    public CategoryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Category save(Category category){
        List<String> sqlColumns = new ArrayList<>();
        Map<String, String> columnNameToBindingValue = new LinkedHashMap<>();
        MapSqlParameterSource bindingValues = new MapSqlParameterSource();
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CATEGORY_ID_KEY, CATEGORY_ID_BINDING_KEY, category.getId().toString());
        addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, NAME_KEY, NAME_BINDING_KEY, category.getName());
        if(category.getCreatedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, CREATED_ON_KEY, CREATED_ON_BINDING_KEY, Timestamp.from(category.getCreatedOn().toInstant()));
        }
        if(category.getModifiedOn() != null){
            addSqlItem(sqlColumns, columnNameToBindingValue, bindingValues, MODIFIED_ON_KEY, MODIFIED_ON_BINDING_KEY, Timestamp.from(category.getModifiedOn().toInstant()));
        }

        StringBuilder sql = getSqlInsertStatement("Category", primaryKeys, sqlColumns, columnNameToBindingValue);

        namedParameterJdbcTemplate.update(sql.toString(), bindingValues);

        return category;
    }

    public Category getCategoryById(String id){
        CategoryRowMapper categoryRowMapper = new CategoryRowMapper();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue(CATEGORY_ID_KEY, id);
        try{
            return namedParameterJdbcTemplate.queryForObject(SELECT_CATEGORY_BY_ID, mapSqlParameterSource, categoryRowMapper);
        }
        catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<Category> getCategories(){
        CategoryRowMapper categoryRowMapper = new CategoryRowMapper();
        try{
            return namedParameterJdbcTemplate.query(SELECT_ALL_CATEGORIES, categoryRowMapper);
        }
        catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    public int deleteCategoryById(String category_id){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(CATEGORY_ID_KEY, category_id);
        return namedParameterJdbcTemplate.update(DELETE_CATEGORY_BY_ID, parameterSource);
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

    private static final class CategoryRowMapper implements RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int i) throws SQLException {
            Category category = new Category();
            category.setId(getUUIDFromResultSet(rs, "id"));
            category.setName(rs.getString("name"));
            category.setCreatedOn(createZonedDateTime(rs, "created_on"));
            category.setModifiedOn(createZonedDateTime(rs, "modified_on"));
            return category;
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
