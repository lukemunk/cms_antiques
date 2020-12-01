package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RoleDto {

    private String id;
    private String roleName;
    private String createdOn;
    private String modifiedOn;
    private List<String> rolePermissions;

    public RoleDto(){}

    public RoleDto(UUID id, String roleNameWithPrefix, ZonedDateTime createdOn, ZonedDateTime modifiedOn){
        this.id = id.toString();
        this.roleName = roleNameWithPrefix;
        roleName = roleName.substring(5, roleNameWithPrefix.length());
        if(createdOn != null){
            this.createdOn = createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        }
        if(modifiedOn != null){
            this.modifiedOn = modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        }
    }

    public RoleDto(UUID id, String roleNameWithPrefix, ZonedDateTime createdOn, ZonedDateTime modifiedOn,
                   Map<UUID, Permission> permissionMap){
        this(id, roleNameWithPrefix, createdOn, modifiedOn);
        rolePermissions = getPermissionListFromRoleMap(permissionMap);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public List<String> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<String> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleDto other = (RoleDto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    private List<String> getPermissionListFromRoleMap(Map<UUID, Permission> permissionMap){
        List<String> permissionList = new ArrayList<>();
        for(Permission permission: permissionMap.values()) {
            permissionList.add(permission.getId().toString());
        }
        return permissionList;
    }
}
