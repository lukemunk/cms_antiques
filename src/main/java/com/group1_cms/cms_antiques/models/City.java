package com.group1_cms.cms_antiques.models;

import org.springframework.web.servlet.tags.form.SelectTag;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class City {

    private UUID id;
    private String name;
    private String postalCode;
    private State state;
    private Map<UUID, Address> addressess;
    private ZonedDateTime createdOn;
    private ZonedDateTime modifiedOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Map<UUID, Address> getAddressess() {
        return addressess;
    }

    public void setAddressess(Map<UUID,Address> addressess) {
        this.addressess = addressess;
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
