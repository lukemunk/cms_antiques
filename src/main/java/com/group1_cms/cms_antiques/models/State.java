package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.util.*;

public class State {

    private UUID id;
    private String name;
    private Map<UUID, City> cities;
    private ZonedDateTime createdOn;
    private ZonedDateTime modifiedOn;

    public State(){ }

    public State(String name){
        this.name = name;
    }

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

    public City getCityById(UUID id){
        if(cities != null){
            return cities.get(id);
        }
        return null;
    }

    public Map<UUID, City> getCities()
    {
        if(cities == null){
            cities = new HashMap<>();
        }
        return cities;
    }

    public void setCities(Map<UUID, City> cities) {
        this.cities = cities;
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
