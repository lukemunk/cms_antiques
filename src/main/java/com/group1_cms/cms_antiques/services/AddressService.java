package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Address;
import com.group1_cms.cms_antiques.models.City;
import com.group1_cms.cms_antiques.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.UUID;

public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public Address findAddressByStreetAddressAndCity(String streetAddress, City city){
        return addressRepository.getAddressByStreetAddressAndCity(streetAddress, city);
    }

    public Address saveAddress(Address address, City city){
        Address addressFromDb = addressRepository.getAddressByStreetAddressAndCity(address.getStreetAddress(), city);
        if(addressFromDb == null){
            address.setId(UUID.randomUUID());
            address.setCreatedOn(ZonedDateTime.now());
            address.setModifiedOn(ZonedDateTime.now());
            address.setCity(city);
        }
        else{
            address.setId(addressFromDb.getId());
            address.setCity(city);
            address.setModifiedOn(ZonedDateTime.now());
        }

        return addressRepository.save(address);
    }

    public int deleteAddress(Address address){
        Address addressToDelete = addressRepository.getAddressByStreetAddressAndCity(address.getStreetAddress(), address.getCity());
        if(addressToDelete == null){
            return 0;
        }
        else{
            return addressRepository.deleteAddress(address);
        }
    }
}
