package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Permission {

    private UUID id;
    private String name;
    private Map<UUID, Role> roles;
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

    public Map<UUID, Role> getRoles() {
        return roles;
    }

    public void setRoles(Map<UUID, Role> roles) {
        this.roles = roles;
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
