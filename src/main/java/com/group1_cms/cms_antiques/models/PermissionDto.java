package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionDto {

    private String id;
    private String permissionName;
    private String createdOn;
    private String modifiedOn;
    private List<String> permissionRoles;

    public PermissionDto(){}

    public PermissionDto(UUID id, String permissionName, ZonedDateTime createdOn, ZonedDateTime modifiedOn){
        this.id = id.toString();
        this.permissionName = permissionName.replace("_", " ");
        this.createdOn = createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.modifiedOn = modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
    }

    public PermissionDto(UUID id, String permissionName, ZonedDateTime createdOn, ZonedDateTime modifiedOn, Map<UUID, Role> roleMap){
        this(id, permissionName, createdOn, modifiedOn);
        permissionRoles = getRoleListFromRoleMap(roleMap);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<String> getPermissionRoles() {
        if(permissionRoles == null){
            permissionRoles = new ArrayList<>();
        }
        return permissionRoles;
    }

    public void setPermissionRoles(List<String> permissionRoles) {
        this.permissionRoles = permissionRoles;
    }

    private List<String> getRoleListFromRoleMap(Map<UUID, Role> roleMap){
        List<String> permissionRoleList = new ArrayList<>();
        for(Role role: roleMap.values()) {
            permissionRoleList.add(role.getId().toString());
        }
        return permissionRoleList;

    }
}
