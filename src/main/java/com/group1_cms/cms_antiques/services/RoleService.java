package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    public boolean checkForDuplicateName(String name){
        String roleName = "ROLE_".concat(name);
        if(findRoleByName(roleName) == null){
            return false;
        }
        return true;
    }

    public Role findRoleWithPermissionsByName(String name){
        return roleRepository.getRoleWithPermissionsByName(name);
    }

    public Role findRoleByName(String name){
        return roleRepository.getRoleByName(name);
    }

    public List<RoleDto> getAllRoles(){
        List<RoleDto> roleDtoList = new ArrayList<>();
        for(Role role : roleRepository.getRoles()){
            roleDtoList.add(new RoleDto(role.getId(), role.getName(), role.getCreatedOn(), role.getModifiedOn()));
        }
        return roleDtoList;
    }

    public Role findRoleById(String id){

        Role roleFromDb = roleRepository.findRoleById(id);
        if(roleFromDb.getId() != null){
            return roleFromDb;
        }
        return null;
    }

    public RoleDto findRoleDtoFromRole(String id){

        Role roleFromDb = roleRepository.findRoleById(id);
        if(roleFromDb.getId() != null){
            return new RoleDto(roleFromDb.getId(), roleFromDb.getName(), roleFromDb.getCreatedOn(), roleFromDb.getModifiedOn(), roleFromDb.getPermissions());
        }
        return null;
    }

    public Role saveRole(Role role){
        Role roleFromDb = findRoleByName(role.getName()); //Get the role from the database by name
        if(roleFromDb == null){                           //roleToSave will be null if the roll isn't in database
            role.setId(UUID.randomUUID());
            role.setCreatedOn(ZonedDateTime.now());
            role.setModifiedOn(ZonedDateTime.now());//save the Role to database
        }
        else{
            role.setId(roleFromDb.getId());
            role.setModifiedOn(ZonedDateTime.now());
        }
        return roleRepository.save(role);
    }

    public void saveRoleWithPermissionsFromRoleDto(RoleDto role){
        Role roleFromDb;

        if(role.getId() == null){
            roleFromDb = new Role();
            roleFromDb.setId(UUID.randomUUID());
            roleFromDb.setCreatedOn(ZonedDateTime.now());
        }
        else{
            roleFromDb = findRoleById(role.getId());
            roleRepository.deleteRole_Permission(role.getId());
        }
        roleFromDb.setName("ROLE_".concat(role.getRoleName()));
        roleFromDb.setModifiedOn(ZonedDateTime.now());
        roleRepository.save(roleFromDb);
        for(String permission_id: role.getRolePermissions()){
            permissionRepository.addToRole_Permission(permission_id, roleFromDb.getId().toString());
        }
    }

    public boolean deleteRoleFromDbById(String id){
        if(roleRepository.deleteRoleById(id) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public void addPermissionsToRole(Role role, List<Permission> permissionList){
        Permission permissionToAdd;
        if(findRoleByName(role.getName()) == null){                 //check to see if role is already in database
            saveRole(role);                                         //save the role if not in database
            for (Permission permission: permissionList) {           //after saving role to database, loop through list of permissions
                permissionToAdd = permissionRepository.getPermissionByName(permission.getName());
                if(permissionToAdd == null){ //check to see if this permession is in database
                    permission.setId(UUID.randomUUID()); //set the random id of permission before saving
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
                    permission.setId(UUID.randomUUID());
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
