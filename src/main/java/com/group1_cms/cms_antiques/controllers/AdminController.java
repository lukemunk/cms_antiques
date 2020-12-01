package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.*;
import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final CategoryService categoryService;
    private final StateService stateService;
    private final UpdatePermissionFormValidator updatePermissionFormValidator;
    private final UpdateRoleFormValidator updateRoleFormValidator;
    private final UpdateStateFormValidator updateStateFormValidator;
    private final UpdateCategoryFormValidator updateCategoryFormValidator;
    private final UpdateUserFormValidator updateUserFormValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PermissionService permissionService,
                           CategoryService categoryService, StateService stateService,
                           UpdatePermissionFormValidator updatePermissionFormValidator, UpdateRoleFormValidator updateRoleFormValidator,
                           UpdateStateFormValidator updateStateFormValidator, UpdateCategoryFormValidator updateCategoryFormValidator,
                           UpdateUserFormValidator updateUserFormValidator){

        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.categoryService = categoryService;
        this.stateService = stateService;
        this.updatePermissionFormValidator = updatePermissionFormValidator;
        this.updateRoleFormValidator = updateRoleFormValidator;
        this.updateStateFormValidator = updateStateFormValidator;
        this.updateCategoryFormValidator = updateCategoryFormValidator;
        this.updateUserFormValidator = updateUserFormValidator;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/getUsersTable", produces = "application/json")
    public @ResponseBody List<UserDataDto> getViewUsersTable(){return userService.getUsersData();}

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/viewUsers")
    public ModelAndView viewRestrictedUsers(){return new ModelAndView("admin/viewRestricted/viewUsers");}

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/getRolesTable", produces = "application/json")
    public @ResponseBody List<RoleDto> getRolesTable(){
        return roleService.getAllRoles();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/viewRoles")
    public ModelAndView viewRoles(){
        return new ModelAndView("admin/viewRestricted/viewRoles");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/getPermissionsTable", produces = "application/json")
    public  @ResponseBody List<PermissionDto> getPermissionsTable(){return permissionService.getAllPermissions(); }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/viewPermissions")
    public ModelAndView viewPermissions(){return new ModelAndView("admin/viewRestricted/viewPermissions");}

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/getCategoriesTable", produces = "application/json")
    public  @ResponseBody List<CategoryDto> getCategoriesTable(){return categoryService.getAllCategories(); }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/viewCategories")
    public ModelAndView viewCategories(){return new ModelAndView("admin/viewRestricted/viewCategories");}

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/getStatesTable")
    public @ResponseBody List<StateDto> getStatesTable(){
        return stateService.findAllStates();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/viewRestricted/viewStates")
    public ModelAndView viewStates(){return new ModelAndView("admin/viewRestricted/viewStates");}

    @RequestMapping(method = RequestMethod.GET, value = "/admin/createUpdate/user/{id}")
    public ModelAndView updateUser(@PathVariable("id")String id){
        UserDataDto user;
        List<RoleDto> roles = roleService.getAllRoles();

        if(id.equals("0")){
            user = new UserDataDto();
        }
        else{
            user = userService.getUserDataDtoByUser(id);
        }

        ModelAndView modelAndView = new ModelAndView("admin/createUpdate/createUpdateUser");
        modelAndView.addObject("updateUserForm", user);
        modelAndView.addObject("roles", roles);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value= "/admin/createUpdate/user/{id}")
    public ModelAndView saveUpdatedUser(@PathVariable("id")String id, @ModelAttribute("updateUserForm") UserDataDto user, BindingResult bindingResult){
        user.setId(id);
        updateUserFormValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateUser").addObject("roles", roleService.getAllRoles());
        }
        userService.saveUserWithRolesFromUserDataDto(user);
        return new ModelAndView("redirect:/admin/viewRestricted/viewUsers");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/user/{id}")
    public Map<String, String> deleteUser(@PathVariable("id") String id){
        String successMessage = "User was succesfully deleted from the database.";
        String failureMessage = "There was a problem deleting the user from the database.";
        if(userService.deleteUserFromDbById(id)){
            return Collections.singletonMap("response", successMessage);
        }
        else{
            return Collections.singletonMap("response", failureMessage);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/createUpdate/role/{id}")
    public ModelAndView updateRole(@PathVariable("id") String id){
        RoleDto role;
        List<PermissionDto> permissions = permissionService.getAllPermissions();

        if(id.equals("0")){
            role = new RoleDto();
        }
        else{
            role = roleService.findRoleDtoFromRole(id);
        }

        ModelAndView modelAndView = new ModelAndView("admin/createUpdate/createUpdateRole");
        modelAndView.addObject("updateRoleForm", role);
        modelAndView.addObject("permissions", permissions);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value= "/admin/createUpdate/role/{id}")
    public ModelAndView saveUpdatedRole(@PathVariable("id")String id, @ModelAttribute("updateRoleForm")RoleDto role, BindingResult bindingResult){
        role.setId(id);
        updateRoleFormValidator.validate(role, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("/admin/createUpdate/createUpdateRole").addObject("permissions", permissionService.getAllPermissions());
        }
        roleService.saveRoleWithPermissionsFromRoleDto(role);
        return new ModelAndView("redirect:/admin/viewRestricted/viewRoles");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/role/{id}")
    public Map<String, String> deleteRole(@PathVariable("id") String id){
        String successMessage = "Role was succesfully deleted from the database.";
        String failureMessage = "There was a problem deleting the role from the database.";
        if(roleService.deleteRoleFromDbById(id)){
            return Collections.singletonMap("response", successMessage);
        }
        else{
            return Collections.singletonMap("response", failureMessage);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/createUpdate/permission/{id}")
    public ModelAndView updatePermission(@PathVariable("id") String id){
        PermissionDto permission;
        List<RoleDto> roles = roleService.getAllRoles();

        if(id.equals("0")){
            permission = new PermissionDto();
        }
        else{
            permission = permissionService.findPermissionDtoFromPermission(id);
        }

        ModelAndView modelAndView = new ModelAndView("admin/createUpdate/createUpdatePermission");
        modelAndView.addObject("updatePermissionForm", permission);
        modelAndView.addObject("roles", roles);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value= "/admin/createUpdate/permission/{id}")
    public ModelAndView saveUpdatedPermission(@PathVariable("id")String id, @ModelAttribute("updatePermissionForm")PermissionDto permission, BindingResult bindingResult){
        permission.setId(id);
        updatePermissionFormValidator.validate(permission, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdatePermission").addObject("roles",roleService.getAllRoles());
        }
        permissionService.savePermissionWithRoles(permission);
        return new ModelAndView("redirect:/admin/viewRestricted/viewPermissions");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/permission/{id}")
    public Map<String, String> deletePermission(@PathVariable("id") String id){
        String successMessage = "Permission was succesfully deleted from the database.";
        String failureMessage = "There was a problem deleting the permission from the database.";
        if(permissionService.deletePermissionFromDbById(id)){
            return Collections.singletonMap("response", successMessage);
        }
        else{
            return Collections.singletonMap("response", failureMessage);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/createUpdate/state/{id}")
    public ModelAndView updateState(@PathVariable("id") String id){
        StateDto state;

        if(id.equals("0")){
            state = new StateDto();
        }
        else{
            state = stateService.findStateById(id);
        }
        ModelAndView modelAndView = new ModelAndView("admin/createUpdate/createUpdateState");
        modelAndView.addObject("updateStateForm", state);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value= "/admin/createUpdate/state/{id}")
    public ModelAndView saveUpdatedState(@PathVariable("id")String id, @ModelAttribute("updateStateForm") StateDto state, BindingResult bindingResult){
        state.setId(id);
        updateStateFormValidator.validate(state, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateState");
        }
        stateService.saveState(state);
        return new ModelAndView("redirect:/admin/viewRestricted/viewStates");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/state/{id}")
    public Map<String, String> deleteState(@PathVariable("id") String id){
        String successMessage = "State was succesfully deleted from the database.";
        String failureMessage = "There was a problem deleting the state from the database.";
        if(stateService.deleteStateFromDbById(id)){
            return Collections.singletonMap("response", successMessage);
        }
        else{
            return Collections.singletonMap("response", failureMessage);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/createUpdate/category/{id}")
    public ModelAndView updateCategory(@PathVariable("id") String id){
        CategoryDto category;

        if(id.equals("0")){
            category = new CategoryDto();
        }
        else{
            category = categoryService.getCategoryById(id);
        }

        ModelAndView modelAndView = new ModelAndView("admin/createUpdate/createUpdateCategory");
        modelAndView.addObject("updateCategoryForm", category);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value= "/admin/createUpdate/category/{id}")
    public ModelAndView saveUpdatedCategory(@PathVariable("id") String id, @ModelAttribute("updateCategoryForm")CategoryDto category, BindingResult bindingResult){
        category.setId(id);
        updateCategoryFormValidator.validate(category, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateCategory");
        }
        categoryService.saveCategory(category);
        return new ModelAndView("redirect:/admin/viewRestricted/viewCategories");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/category/{id}")
    public Map<String, String> deleteCategory(@PathVariable("id") String id){
        String successMessage = "Category was succesfully deleted from the database.";
        String failureMessage = "There was a problem deleting the category from the database.";
        if(categoryService.deleteCategoryFromDbById(id)){
            return Collections.singletonMap("response", successMessage);
        }
        else{
            return Collections.singletonMap("response", failureMessage);
        }
    }
}
