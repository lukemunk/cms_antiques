package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class PermissionService {

    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public Permission findPermissionByName(String name){
        return permissionRepository.getPermissionByName(name);
    }

    public Permission savePermission(Permission permission){
        Permission permissionToSave = findPermissionByName(permission.getName());
        if(permissionToSave == null){
            permission.setId(UUID.randomUUID());
            permissionToSave = permissionRepository.save(permission);
        }
        return permissionToSave;
    }
}
