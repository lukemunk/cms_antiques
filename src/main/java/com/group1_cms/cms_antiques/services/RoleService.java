package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class RoleService {

    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role findRoleWithPermissionsByName(String name){
        return roleRepository.getRoleWithPermissionsByName(name);
    }

    public Role findRoleByName(String name){
        return roleRepository.getRoleByName(name);
    }

    public Role saveRole(Role role){
        Role roleToSave = findRoleByName(role.getName()); //Get the role from the database by name
        if(roleToSave == null){                           //roleToSave will be null if the roll isn't in database
            role.setId(UUID.randomUUID());
            roleToSave = roleRepository.save(role);       //save the Role to database
        }
        return roleToSave;
    }

    public void addPermissionsToRole(Role role, List<Permission> permissionList){
        Permission permissionToAdd;
        if(findRoleByName(role.getName()) == null){                 //check to see if role is already in database
            saveRole(role);                                         //save the role if not in database
            for (Permission permission: permissionList) {           //after saving role to database, loop through list of permissions
                permissionToAdd = permissionRepository.getPermissionByName(permission.getName());
                if(permissionToAdd == null){ //check to see if this permession is in database
                    permissionRepository.save(permission);                                  //save the permission if not in database
                    roleRepository.addPermissions(role, permission);   //after saving Role and Permission to database add them to Role_Permission
                }
                else{
                    roleRepository.addPermissions(role, permissionToAdd); //add permission already in database along with saved role to Role_Permission
                }
            }
        }
        else{
            Role roleWithPermissions;
            roleWithPermissions = roleRepository.getRoleWithPermissionsByName(role.getName()); //if role is already in database get the associated permissions
            for (Permission permission: permissionList){     //loop through the permissions
                permissionToAdd = permissionRepository.getPermissionByName(permission.getName());
                if(permissionToAdd == null){                                                  //check if permission is already in database
                    permissionRepository.save(permission);                                    //if permission not in database save it
                    roleRepository.addPermissions(roleWithPermissions, permission);           //add role and permission to Role_Permission in database
                }
                else{
                    if(roleWithPermissions.getPermissionById(permissionToAdd.getId()) == null){ //check if this role is already associated with this permission
                        roleRepository.addPermissions(roleWithPermissions, permissionToAdd);    //if not add role and permission to Role_Permission
                        roleWithPermissions.getPermissions().put(permissionToAdd.getId(), permissionToAdd); //add permission to role map permission with permission id as key
                    }
                    else{
                        return; //if role is already associated with the permission return
                    }
                }
            }
        }
    }
}
