package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Role {

    private UUID id;
    private String name;
    private Map<UUID, User> users;
    private Map<UUID, Permission> permissions;
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

    public Map<UUID, User> getUsers() {
        return users;
    }

    public void setUsers(Map<UUID, User> users) {
        this.users = users;
    }

    public Permission getPermissionById(UUID id){
        if(permissions != null){
            return permissions.get(id);
        }
        return null;
    }

    public Map<UUID, Permission> getPermissions() {
        if(permissions == null){
            permissions = new HashMap<>();
        }
        return permissions;
    }

    public void setPermissions(Map<UUID, Permission> permissions) {
        this.permissions = permissions;
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
