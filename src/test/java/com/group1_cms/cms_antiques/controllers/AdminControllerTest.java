package com.group1_cms.cms_antiques.controllers;
import com.group1_cms.cms_antiques.components.*;
import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.services.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Assert;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import org.springframework.security.core.Authentication;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.*;



@RunWith(MockitoJUnitRunner.class)
class AdminControllerTest {

    private UpdateCategoryFormValidator updateCategoryFormValidator = Mockito.mock(UpdateCategoryFormValidator.class);
    private UpdatePermissionFormValidator updatePermissionFormValidator = Mockito.mock(UpdatePermissionFormValidator.class);
    private UpdateRoleFormValidator updateRoleFormValidator = Mockito.mock(UpdateRoleFormValidator.class);
    private UpdateStateFormValidator updateStateFormValidator = Mockito.mock(UpdateStateFormValidator.class);
    private UpdateUserFormValidator updateUserFormValidator = Mockito.mock(UpdateUserFormValidator.class);

    private UserService userService = Mockito.mock(UserService.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private PermissionService permissionService = Mockito.mock(PermissionService.class);
    private CategoryService categoryService = Mockito.mock(CategoryService.class);
    private StateService stateService = Mockito.mock(StateService.class);
    private Authentication auth = Mockito.mock(Authentication.class);

    private AdminController adminController = new AdminController(userService, roleService, permissionService, categoryService,
            stateService, updatePermissionFormValidator, updateRoleFormValidator, updateStateFormValidator, updateCategoryFormValidator,
            updateUserFormValidator);



    //classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);

    @Test
    public void getTableUser(){
        Map<UUID, Role> roleMap = new HashMap<>();
        Role role = new Role();
        UUID roleId = UUID.randomUUID();
        UUID user1Id = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();
        role.setId(roleId);
        role.setName("ROLE_Member");
        role.setCreatedOn(ZonedDateTime.now());
        role.setModifiedOn(ZonedDateTime.now());
        roleMap.put(roleId, role);
        List<UserDataDto> userList = new ArrayList<>();
        List<UserDataDto> testUserList = new ArrayList<>();
        UserDataDto user1 = new UserDataDto(user1Id, "first", "last", "user1",
                "user@gmail.com", ZonedDateTime.now(), false, roleMap);
        UserDataDto user2 = new UserDataDto(user2Id, "first2", "last2", "user2", "user2@gmail.com", ZonedDateTime.now(),
                false, roleMap);
        userList.add(user1);
        userList.add(user2);

        when(userService.getUsersData()).thenReturn(userList);

        testUserList = adminController.getViewUsersTable();

        Assert.assertEquals(testUserList, userList);

    }

    @Test
    public void getTableRole(){
        UUID roleId1 = UUID.randomUUID();
        UUID roleId2 = UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();
        Permission permission = new Permission();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        permission.setName("View_Classifieds");
        permission.setId(permissionId);
        permission.setCreatedOn(createdOn);
        permission.setModifiedOn(modifiedOn);
        Map<UUID, Permission> permissionMap = new HashMap<>();
        permissionMap.put(permissionId, permission);
        List<RoleDto> roleDtoList = new ArrayList();
        RoleDto role1 = new RoleDto(roleId1, "ROLE_Admin", createdOn, modifiedOn, permissionMap);
        RoleDto role2 = new RoleDto(roleId2, "ROLE_Member", createdOn, modifiedOn, permissionMap);
        roleDtoList.add(role1);
        roleDtoList.add(role2);

        List<RoleDto> testRoleDto = new ArrayList<>();

        when(roleService.getAllRoles()).thenReturn(roleDtoList);

        testRoleDto = adminController.getRolesTable();

        Assert.assertEquals(roleDtoList, testRoleDto);
    }


    @Test
    public void getTablePermission(){
        UUID permissionId1 = UUID.randomUUID();
        UUID permissionId2 = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        role.setName("ROLE_Admin");
        role.setId(roleId);
        role.setCreatedOn(createdOn);
        role.setModifiedOn(modifiedOn);
        Map<UUID, Role> roleMap = new HashMap<>();
        roleMap.put(roleId, role);
        List<PermissionDto> permissionDtoList = new ArrayList();
        PermissionDto permission1 = new PermissionDto(permissionId1, "View_Classifieds", createdOn, modifiedOn, roleMap);
        PermissionDto permission2 = new PermissionDto(permissionId2, "Modify_User", createdOn, modifiedOn, roleMap);
        permissionDtoList.add(permission1);
        permissionDtoList.add(permission2);

        List<PermissionDto> testPermissionDto = new ArrayList<>();

        when(permissionService.getAllPermissions()).thenReturn(permissionDtoList);

        testPermissionDto = adminController.getPermissionsTable();

        Assert.assertEquals(permissionDtoList, testPermissionDto);
    }

    @WithMockUser(value = "admin")
    @Test
    public void getTableCategory(){
        UUID category1Id = UUID.randomUUID();
        UUID category2Id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        CategoryDto category1 = new CategoryDto(category1Id, "Tools", createdOn, modifiedOn);
        CategoryDto category2 = new CategoryDto(category2Id, "Furniture", createdOn, modifiedOn);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(category1);
        categoryDtoList.add(category2);

        List<CategoryDto> testCategoryDtoList = new ArrayList();

        when(categoryService.getAllCategories()).thenReturn(categoryDtoList);

        testCategoryDtoList = adminController.getCategoriesTable();

        Assert.assertEquals(categoryDtoList, testCategoryDtoList);
    }

    @Test
    public void getTableState(){
        UUID state1Id = UUID.randomUUID();
        UUID state2Id = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        StateDto state1 = new StateDto(state1Id, "Utah", createdOn, modifiedOn);
        StateDto state2 = new StateDto(state2Id, "Idaho", createdOn, modifiedOn);
        List<StateDto> stateDtoList = new ArrayList<>();
        stateDtoList.add(state1);
        stateDtoList.add(state2);

        List<StateDto> testStateDtoList = new ArrayList();

        when(stateService.findAllStates()).thenReturn(stateDtoList);

        testStateDtoList = adminController.getStatesTable();

        Assert.assertEquals(stateDtoList, testStateDtoList);
    }

    @Test
    public void viewUsers(){
        ModelAndView newView = adminController.viewRestrictedUsers();
        Assert.assertEquals(newView.getViewName(), "admin/viewRestricted/viewUsers");
    }

    @Test
    public void viewRoles(){
        ModelAndView newView = adminController.viewRoles();
        Assert.assertEquals(newView.getViewName(), "admin/viewRestricted/viewRoles");
    }

    @Test
    public void viewPermissions(){
        ModelAndView newView = adminController.viewPermissions();
        Assert.assertEquals(newView.getViewName(), "admin/viewRestricted/viewPermissions");
    }

    @Test
    public void viewCategories(){
        ModelAndView newView = adminController.viewCategories();
        Assert.assertEquals(newView.getViewName(), "admin/viewRestricted/viewCategories");
    }

    @Test
    public void viewStates(){
        ModelAndView newView = adminController.viewStates();
        Assert.assertEquals(newView.getViewName(), "admin/viewRestricted/viewStates");
    }

    @Test
    public void viewUser(){
        Map<UUID, Role> roleMap = new HashMap<>();
        Role role = new Role();
        UUID id = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        ZonedDateTime createdOn = ZonedDateTime.now();
        ZonedDateTime modifiedOn = ZonedDateTime.now();
        role.setId(roleId);
        role.setName("ROLE_Member");
        role.setCreatedOn(ZonedDateTime.now());
        role.setModifiedOn(ZonedDateTime.now());
        roleMap.put(roleId, role);
        UserDataDto user = new UserDataDto(id, "first", "last", "user", "user@gmail.com", modifiedOn, false, roleMap);

        when(userService.getUserDataDtoByUser(id.toString())).thenReturn(user);
        ModelAndView newView = adminController.updateUser(id.toString());

        Assert.assertEquals(newView.getViewName(), "admin/createUpdate/createUpdateUser");
        Assert.assertEquals(newView.getModel().get("updateUserForm"), user);

    }
}
