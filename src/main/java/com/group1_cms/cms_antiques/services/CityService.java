package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.repositories.CityRepository;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository){
        this.cityRepository = cityRepository;
    }

    public City findCityByNameAndPostalCode(String cityName, String stateName, String postalCode){
        return cityRepository.getCityByNameAndStateAndPostalCode(cityName, stateName, postalCode);
    }

    public City saveCity(City city, State state){
        City cityFromDb = cityRepository.getCityByNameAndStateAndPostalCode(city.getName(), state.getName(), city.getPostalCode());
        if(cityFromDb == null){
            city.setId(UUID.randomUUID());
            city.setCreatedOn(ZonedDateTime.now());
            city.setModifiedOn(ZonedDateTime.now());
            city.setState(state);
        }
        else{
            city.setId(cityFromDb.getId());
            city.setState(state);
            city.setModifiedOn(ZonedDateTime.now());
        }
        return cityRepository.save(city);
    }

}
