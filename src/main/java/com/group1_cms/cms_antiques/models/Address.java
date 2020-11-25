package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class Address {

    private UUID id;
    private String streetAddress;
    private String streetAddressLine2;
    private City city;
    private Map<UUID, User> users;
    private ZonedDateTime createdOn;
    private ZonedDateTime modifiedOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddressLine2() {
        return streetAddressLine2;
    }

    public void setStreetAddressLine2(String streetAddressLine2) {
        this.streetAddressLine2 = streetAddressLine2;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Map<UUID, User> getUsers() {
        return users;
    }

    public void setUsers(Map<UUID, User> users) {
        this.users = users;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(ZonedDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
