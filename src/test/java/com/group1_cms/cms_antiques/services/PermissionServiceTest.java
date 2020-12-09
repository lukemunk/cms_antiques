package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.repositories.PermissionRepository;
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
class PermissionServiceTest {

    private PermissionRepository permissionRepository = Mockito.mock(PermissionRepository.class);
    private PermissionService permissionService = new PermissionService(permissionRepository);

    @Test
    public void checkForDuplicateName(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Permission permissionToTest = new Permission();
        permissionToTest.setName("View_Classifieds");
        permissionToTest.setId(id);
        permissionToTest.setCreatedOn(createdOn);
        permissionToTest.setModifiedOn(modifiedOn);
        when(permissionRepository.getPermissionByName(permissionToTest.getName())).thenReturn(permissionToTest);

        boolean isDuplicate = permissionService.checkForDuplicateName("View_Classifieds");
        boolean isNotDuplicate = permissionService.checkForDuplicateName("View_Restricted");
        assertEquals(true, isDuplicate);
        assertEquals(false, isNotDuplicate);
    }

    @Test
    public void findPermissionByName(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Permission permissionToTest = new Permission();
        permissionToTest.setName("View_Restricted");
        permissionToTest.setId(id);
        permissionToTest.setCreatedOn(createdOn);
        permissionToTest.setModifiedOn(modifiedOn);
        when(permissionRepository.getPermissionByName("View_Restricted")).thenReturn(permissionToTest);

        Permission permission = permissionService.findPermissionByName("View_Restricted");
        assertEquals("View_Restricted", permission.getName());
        assertEquals(id, permission.getId());
        assertEquals(createdOn, permission.getCreatedOn());
        assertEquals(modifiedOn, permission.getModifiedOn());
    }

    @Test
    public void findPermissionDtoFromPermission(){
        UUID id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Permission permissionToTest = new Permission();
        permissionToTest.setName("View_Restricted");
        permissionToTest.setId(id);
        permissionToTest.setCreatedOn(createdOn);
        permissionToTest.setModifiedOn(modifiedOn);
        when(permissionRepository.getPermissionById(id.toString())).thenReturn(permissionToTest);

        PermissionDto permission = permissionService.findPermissionDtoFromPermission(id.toString());
        assertEquals("View_Restricted", permission.getPermissionName().replaceAll("\\s+", "_"));
        assertEquals(id.toString(), permission.getId());
        assertEquals(createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), permission.getCreatedOn());
        assertEquals(modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu")), permission.getModifiedOn());
    }

    @Test
    public void getAllPermissions(){
        List<Permission> permissionList = new ArrayList<>();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();

        Permission permission1 = new Permission();
        Permission permission2 = new Permission();
        Permission permission3 = new Permission();

        permission1.setId(id1);
        permission1.setName("View_Restricted");
        permission1.setCreatedOn(createdOn);
        permission1.setModifiedOn(modifiedOn);

        permission2.setId(id2);
        permission2.setName("View_Classifieds");
        permission2.setCreatedOn(createdOn);
        permission2.setModifiedOn(modifiedOn);

        permission3.setId(id3);
        permission3.setName("Modify_User");
        permission3.setCreatedOn(createdOn);
        permission3.setModifiedOn(modifiedOn);

        permissionList.add(permission1);
        permissionList.add(permission2);
        permissionList.add(permission3);

        when(permissionRepository.getAllPermissions()).thenReturn(permissionList);

        List<PermissionDto> permissionDtos = permissionService.getAllPermissions();

        assertEquals(3, permissionDtos.size());
        verify(permissionRepository, times(1)).getAllPermissions();
        for(int i = 0; i < permissionDtos.size(); i++){
            String permissionName = permissionDtos.get(i).getPermissionName().replaceAll("\\s+", "_");
            assertEquals(permissionList.get(i).getName(), permissionName);
        }
    }

    @Test
    public void savePermissionWithRoles(){
        UUID id = UUID.randomUUID();

        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        String permissionName = "View_Classifies";
        Role newRole= new Role();
        UUID roleId = UUID.randomUUID();
        String roleName = "ROLE_Admin";
        newRole.setId(roleId);
        newRole.setName(roleName);
        newRole.setCreatedOn(createdOn);
        newRole.setModifiedOn(modifiedOn);

        Map<UUID, Role> roleMap = new HashMap<>();
        roleMap.put(roleId, newRole);
        PermissionDto permissionDto = new PermissionDto(id, roleName, createdOn, modifiedOn, roleMap);

        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(roleName);
        permission.setCreatedOn(createdOn);
        permission.setModifiedOn(modifiedOn);
        when(permissionRepository.getPermissionById(permission.getId().toString())).thenReturn(permission);

        permissionService.savePermissionWithRoles(permissionDto);

        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    public void deletePermissionFromDbById(){
        Permission newPermission = new Permission();
        newPermission.setName("View_Classifieds");
        newPermission.setId(UUID.randomUUID());
        when(permissionRepository.deletePermissionById(newPermission.getId().toString())).thenReturn(1);
        assertEquals(false, permissionService.deletePermissionFromDbById(UUID.randomUUID().toString()));
        assertEquals(true, permissionService.deletePermissionFromDbById(newPermission.getId().toString()));
    }
}
