package com.group1_cms.cms_antiques.repositories;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CityRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CityRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
