package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Address;
import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.repositories.AddressRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class AddressServiceTest
{
    private AddressRepository addressRepository;

    @Test
    void findAddressByStreetAddressAndCity()
    {
        addressRepository = Mockito.mock(AddressRepository.class);
        AddressService newService = new AddressService(addressRepository);
        City newCity = new City();
        Address newAddy = new Address();
        Mockito.when(addressRepository.getAddressByStreetAddressAndCity("15", newCity)).thenReturn(newAddy);
        newService.findAddressByStreetAddressAndCity("15", newCity);

    }

    @Test
    void saveAddress()
    {
        addressRepository = Mockito.mock(AddressRepository.class);
        AddressService newService = new AddressService(addressRepository);
        City newCity = new City();
        Address newAddy = new Address();
        Mockito.when(addressRepository.save(newAddy)).thenReturn(newAddy);
        Assert.assertEquals(newAddy, newService.saveAddress(newAddy, newCity));
    }

    @Test
    void deleteAddress()
    {
        addressRepository = Mockito.mock(AddressRepository.class);
        AddressService newService = new AddressService(addressRepository);
        City newCity = new City();
        Address newAddy = new Address();
        Mockito.when(addressRepository.deleteAddress(newAddy)).thenReturn(0);
        Assert.assertEquals(0, newService.deleteAddress(newAddy));
    }
}