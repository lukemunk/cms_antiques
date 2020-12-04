package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.repositories.CityRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class CityServiceTest
{
    private CityRepository cityRepository;

    @Test
    void findCityByNameAndPostalCode()
    {
        cityRepository = Mockito.mock(CityRepository.class);
        CityService newService = new CityService(cityRepository);
        City newCity = new City();

        Assert.assertEquals(null, newService.findCityByNameAndPostalCode("Test", "Utah", "8444") );
    }

    @Test
    void saveCity()
    {
        cityRepository = Mockito.mock(CityRepository.class);
        CityService newService = new CityService(cityRepository);
        City newCity = new City();
        Mockito.when(cityRepository.save(newCity)).thenReturn(newCity);
        Assert.assertEquals(newCity, newService.saveCity(newCity, new State()) );
    }

    @Test
    void saveCity2()
    {
        cityRepository = Mockito.mock(CityRepository.class);
        CityService newService = new CityService(cityRepository);
        State newState = new State();
        newState.setName("Dave");
        City newCity = new City();
        newCity.setName("Test");
        newCity.setPostalCode("844");
        Mockito.when(cityRepository.save(newCity)).thenReturn(newCity);
        Mockito.when(cityRepository.getCityByNameAndStateAndPostalCode(newCity.getName(), newState.getName(), newCity.getPostalCode())).thenReturn(newCity);
        Assert.assertEquals(newCity, newService.saveCity(newCity, newState) );
    }
}