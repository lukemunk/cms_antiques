package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class Category {

    private UUID id;
    private String name;
    private Map<UUID, Item> items;
    private ZonedDateTime createdOn;
    private ZonedDateTime modifiedOn;

    public Category(){ };

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

    public Map<UUID, Item> getItems() {
        return items;
    }

    public void setItems(Map<UUID, Item> items) {
        this.items = items;
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
