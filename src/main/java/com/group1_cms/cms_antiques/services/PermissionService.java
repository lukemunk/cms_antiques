package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionService {

    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public boolean checkForDuplicateName(String name){
        String permissionName = name.replaceAll("\\s+", "_");
        if(findPermissionByName(permissionName) == null){
            return false;
        }
        return true;
    }

    public Permission findPermissionByName(String name){
        return permissionRepository.getPermissionByName(name);
    }
/*
    public Permission findPermissionById(String id){
        Permission permissionFromDb = permissionRepository.getPermissionById(id);
        if(permissionFromDb.getId() != null){
            return permissionFromDb;
        }
        return null;
    } */

    public PermissionDto findPermissionDtoFromPermission(String id){
        Permission permissionFromDb = permissionRepository.getPermissionById(id);
        if(permissionFromDb.getId() != null){
            return new PermissionDto(permissionFromDb.getId(), permissionFromDb.getName(), permissionFromDb.getCreatedOn(), permissionFromDb.getModifiedOn(), permissionFromDb.getRoles());
        }
        return null;
    }

    public List<PermissionDto> getAllPermissions(){
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        for(Permission permission: permissionRepository.getAllPermissions()){
            permissionDtoList.add(new PermissionDto(permission.getId(), permission.getName(),
                    permission.getCreatedOn(), permission.getModifiedOn()));
        }
        return permissionDtoList;
    }

    public void savePermissionWithRoles(PermissionDto permission){
        Permission permissionFromDb;

        if(permission.getId() == null){
            permissionFromDb = new Permission();
            permissionFromDb.setId(UUID.randomUUID());
            permissionFromDb.setCreatedOn(ZonedDateTime.now());
        }
        else{
            permissionFromDb = permissionRepository.getPermissionById(permission.getId());
            permissionRepository.deleteRole_Permission(permission.getId());
        }
        permissionFromDb.setName(permission.getPermissionName().replaceAll("\\s+", "_"));
        permissionFromDb.setModifiedOn(ZonedDateTime.now());

        permissionRepository.save(permissionFromDb);
        for(String role_id: permission.getPermissionRoles()){
            permissionRepository.addToRole_Permission(permissionFromDb.getId().toString(), role_id);
        }
    }

    public boolean deletePermissionFromDbById(String id){
        if(permissionRepository.deletePermissionById(id) > 0){
            return true;
        }
        else{
            return false;
        }
    }
/*
    public Permission savePermission(Permission permission){
        Permission permissionFromDb = findPermissionByName(permission.getName());
        if(permissionFromDb == null){
            permission.setId(UUID.randomUUID());
            permission.setCreatedOn(ZonedDateTime.now());
            permission.setModifiedOn(ZonedDateTime.now());
        }
        else{
            permission.setId(permissionFromDb.getId());
            permission.setModifiedOn(ZonedDateTime.now());
        }
        return permissionRepository.save(permission);
    }*/
}
