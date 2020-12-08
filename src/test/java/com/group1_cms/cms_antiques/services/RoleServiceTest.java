package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class RoleServiceTest {

    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private RoleService roleService = new RoleService(roleRepository, permissionRepository);

    @Test
    public void findRoleByName(){

        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role roleToTest = new Role();
        roleToTest.setName("ROLE_Admin");
        roleToTest.setId(id);
        roleToTest.setCreatedOn(createdOn);
        roleToTest.setModifiedOn(modifiedOn);
        when(roleRepository.getRoleByName("ROLE_Admin")).thenReturn(roleToTest);

        Role role = roleService.findRoleByName("ROLE_Admin");
        assertEquals("ROLE_Admin", role.getName());
        assertEquals(id, role.getId());
        assertEquals(createdOn, role.getCreatedOn());
        assertEquals(modifiedOn, role.getModifiedOn());
    }

    @Test
    public void checkForDuplicateName(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role roleToTest = new Role();
        roleToTest.setName("ROLE_Admin");
        roleToTest.setId(id);
        roleToTest.setCreatedOn(createdOn);
        roleToTest.setModifiedOn(modifiedOn);
        when(roleRepository.getRoleByName("ROLE_Admin")).thenReturn(roleToTest);

        boolean isDuplicate = roleService.checkForDuplicateName("Admin");
        boolean isNotDuplicate = roleService.checkForDuplicateName("Member");
        assertEquals(true, isDuplicate);
        assertEquals(false, isNotDuplicate);
    }

    @Test
    public void getAllRoles(){
        List<Role> roleList = new ArrayList<>();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role role1 = new Role();
        Role role2 = new Role();
        Role role3 = new Role();

        role1.setId(id1);
        role1.setName("ROLE_Admin");
        role1.setCreatedOn(createdOn);
        role1.setModifiedOn(modifiedOn);

        role2.setId(id2);
        role2.setName("ROLE_Member");
        role2.setCreatedOn(createdOn);
        role2.setModifiedOn(modifiedOn);

        role3.setId(id3);
        role3.setName("ROLE_Moderator");
        role3.setCreatedOn(createdOn);
        role3.setModifiedOn(modifiedOn);

        roleList.add(role1);
        roleList.add(role2);
        roleList.add(role3);

        when(roleRepository.getRoles()).thenReturn(roleList);

        List<RoleDto> roleDtos = roleService.getAllRoles();

        assertEquals(3, roleDtos.size());
        verify(roleRepository, times(1)).getRoles();
        for(int i = 0; i < roleDtos.size(); i++){
            String nameWithPrefix = "ROLE_".concat(roleDtos.get(i).getRoleName());
            assertEquals(roleList.get(i).getName(), nameWithPrefix);
        }
    }

    @Test
    public void findRoleById(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role roleToTest = new Role();
        roleToTest.setName("ROLE_Admin");
        roleToTest.setId(id);
        roleToTest.setCreatedOn(createdOn);
        roleToTest.setModifiedOn(modifiedOn);
        when(roleRepository.findRoleById(id.toString())).thenReturn(roleToTest);

        Role role = roleService.findRoleById(id.toString());
        assertEquals("ROLE_Admin", role.getName());
        assertEquals(id, role.getId());
        assertEquals(createdOn, role.getCreatedOn());
        assertEquals(modifiedOn, role.getModifiedOn());
    }

    @Test
    public void findRoleDtoFromRole(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role roleToTest = new Role();
        roleToTest.setName("ROLE_Admin");
        roleToTest.setId(id);
        roleToTest.setCreatedOn(createdOn);
        roleToTest.setModifiedOn(modifiedOn);
        when(roleRepository.findRoleById(id.toString())).thenReturn(roleToTest);

        RoleDto role = roleService.findRoleDtoFromRole(id.toString());
        assertEquals("ROLE_Admin", "ROLE_".concat(role.getRoleName()));
        assertEquals(id.toString(), role.getId());
        assertEquals(createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), role.getCreatedOn());
        assertEquals(modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), role.getModifiedOn());
    }

    @Test
    public void saveRole(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Role roleToTest = new Role();
        roleToTest.setName("ROLE_Admin");
        roleToTest.setId(id);
        roleToTest.setCreatedOn(createdOn);
        roleToTest.setModifiedOn(modifiedOn);
        Role oldRoleSaved = roleService.saveRole(roleToTest);

        verify(roleRepository, times(1)).save(roleToTest);


        Role newRole = new Role();
        newRole.setName("ROLE_Member");
        newRole.setCreatedOn(createdOn);
        newRole.setModifiedOn(modifiedOn);
        Role newRoleSaved = roleService.saveRole(newRole);

        verify(roleRepository, times(1)).save(newRole);

    }

    @Test
    public void saveRoleWithPermissionsFromRoleDto(){
        UUID id = UUID.randomUUID();

        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        String roleName = "ROLE_Admin";
        Permission newPermission = new Permission();
        UUID permissionId = UUID.randomUUID();
        String permissionName = "View_Classifieds";
        newPermission.setId(permissionId);
        newPermission.setName(permissionName);
        newPermission.setCreatedOn(createdOn);
        newPermission.setModifiedOn(modifiedOn);

        Map<UUID, Permission> permissionMap = new HashMap<>();
        permissionMap.put(permissionId, newPermission);
        RoleDto roleDto = new RoleDto(id, roleName, createdOn, modifiedOn, permissionMap);

        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        role.setCreatedOn(createdOn);
        role.setModifiedOn(modifiedOn);
        when(roleRepository.findRoleById(id.toString())).thenReturn(role);

        roleService.saveRoleWithPermissionsFromRoleDto(roleDto);

        verify(roleRepository, times(1)).save(role);
    }

    @Test
    public void deleteRoleFromDbById(){
        Role newRole = new Role();
        newRole.setName("ROLE_Admin");
        newRole.setId(UUID.randomUUID());
        when(roleRepository.deleteRoleById(newRole.getId().toString())).thenReturn(1);
        assertEquals(false, roleService.deleteRoleFromDbById(UUID.randomUUID().toString()));
        assertEquals(true, roleService.deleteRoleFromDbById(newRole.getId().toString()));
    }

    @Test
    public void  addPermissionsToRole(){
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        Role newRole = new Role();
        newRole.setName("ROLE_Admin");
        newRole.setId(UUID.randomUUID());
        newRole.setCreatedOn(createdOn);
        newRole.setModifiedOn(modifiedOn);
        List<Permission> permissionList = new ArrayList<>();


        UUID permission1Id = UUID.randomUUID();
        UUID permission2Id = UUID.randomUUID();
        String permName1 = "View_Classifieds";
        String permName2 = "View_Private";
        Permission perm1 = new Permission();
        Permission perm2 = new Permission();
        perm1.setId(permission1Id);
        perm1.setCreatedOn(createdOn);
        perm1.setModifiedOn(modifiedOn);
        perm1.setName(permName1);
        perm2.setId(permission2Id);
        perm2.setCreatedOn(createdOn);
        perm2.setModifiedOn(modifiedOn);
        perm2.setName(permName2);
        permissionList.add(perm1);
        permissionList.add(perm2);

        when(roleRepository.getRoleByName("ROLE_Admin")).thenReturn(newRole);
        when(roleRepository.getRoleWithPermissionsByName("ROLE_Admin")).thenReturn(newRole);
        roleService.addPermissionsToRole(newRole, permissionList);

        verify(roleRepository, times(1)).addPermissions(newRole, perm1);
        verify(roleRepository, times(1)).addPermissions(newRole, perm2);
    }
}
