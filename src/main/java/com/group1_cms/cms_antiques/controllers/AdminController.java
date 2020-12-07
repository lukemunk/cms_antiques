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
import java.util.Optional;

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

    @RequestMapping(method = RequestMethod.POST, value= {"/admin/createUpdate/user","/admin/createUpdate/user/{id}"})
    public ModelAndView saveUpdatedUser(@PathVariable(value = "id", required = false)String id, @ModelAttribute("updateUserForm") UserDataDto user, BindingResult bindingResult){
        if(id != null){
            user.setId(id);
        }
        updateUserFormValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateUser").addObject("roles", roleService.getAllRoles());
        }
        userService.saveUserWithRolesFromUserDataDto(user);
        return new ModelAndView("redirect:/admin/viewRestricted/viewUsers");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/user/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable("id") String id){
        Boolean success = true;
        Boolean failure = false;

        if(userService.deleteUserFromDbById(id)){
            return Collections.singletonMap("success", success);
        }
        else{
            return Collections.singletonMap("success", failure);
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

    @RequestMapping(method = RequestMethod.POST, value= {"/admin/createUpdate/role","/admin/createUpdate/role/{id}"})
    public ModelAndView saveUpdatedRole(@PathVariable(value = "id", required = false)String id, @ModelAttribute("updateRoleForm")RoleDto role, BindingResult bindingResult){

        if(id != null){
            role.setId(id);
        }

        updateRoleFormValidator.validate(role, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("/admin/createUpdate/createUpdateRole").addObject("permissions", permissionService.getAllPermissions());
        }
        roleService.saveRoleWithPermissionsFromRoleDto(role);
        return new ModelAndView("redirect:/admin/viewRestricted/viewRoles");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/role/{id}")
    public Map<String, Boolean> deleteRole(@PathVariable("id") String id){
        Boolean success = true;
        Boolean failure = false;
        if(roleService.deleteRoleFromDbById(id)){
            return Collections.singletonMap("success", success);
        }
        else{
            return Collections.singletonMap("success", failure);
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

    @RequestMapping(method = RequestMethod.POST, value= {"/admin/createUpdate/permission", "/admin/createUpdate/permission/{id}"})
    public ModelAndView saveUpdatedPermission(@PathVariable(value = "id", required = false)String id, @ModelAttribute("updatePermissionForm")PermissionDto permission, BindingResult bindingResult){

        if(id != null){
            permission.setId(id);
        }
        updatePermissionFormValidator.validate(permission, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdatePermission").addObject("roles",roleService.getAllRoles());
        }
        permissionService.savePermissionWithRoles(permission);
        return new ModelAndView("redirect:/admin/viewRestricted/viewPermissions");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/permission/{id}")
    public Map<String, Boolean> deletePermission(@PathVariable("id") String id){
        Boolean success = true;
        Boolean failure = false;
        if(permissionService.deletePermissionFromDbById(id)){
            return Collections.singletonMap("success", success);
        }
        else{
            return Collections.singletonMap("success", failure);
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

    @RequestMapping(method = RequestMethod.POST, value= {"/admin/createUpdate/state", "/admin/createUpdate/state/{id}"})
    public ModelAndView saveUpdatedState(@PathVariable(value = "id", required = false)String id, @ModelAttribute("updateStateForm") StateDto state, BindingResult bindingResult){

        if(id != null){
            state.setId(id);
        }
        updateStateFormValidator.validate(state, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateState");
        }
        stateService.saveState(state);
        return new ModelAndView("redirect:/admin/viewRestricted/viewStates");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/state/{id}")
    public Map<String, Boolean> deleteState(@PathVariable("id") String id){
        Boolean success = true;
        Boolean failure = false;
        if(stateService.deleteStateFromDbById(id)){
            return Collections.singletonMap("success", success);
        }
        else{
            return Collections.singletonMap("success", failure);
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

    @RequestMapping(method = RequestMethod.POST, value= {"/admin/createUpdate/category", "/admin/createUpdate/category/{id}"} )
    public ModelAndView saveUpdatedCategory(@PathVariable(value = "id", required = false)String id, @ModelAttribute("updateCategoryForm")CategoryDto category, BindingResult bindingResult){

        if(id != null){
            category.setId(id);
        }
        updateCategoryFormValidator.validate(category, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("admin/createUpdate/createUpdateCategory");
        }
        categoryService.saveCategory(category);
        return new ModelAndView("redirect:/admin/viewRestricted/viewCategories");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/delete/category/{id}")
    public Map<String, Boolean> deleteCategory(@PathVariable("id") String id){
        Boolean success = true;
        Boolean failure = false;
        if(categoryService.deleteCategoryFromDbById(id)){
            return Collections.singletonMap("success", success);
        }
        else{
            return Collections.singletonMap("success", failure);
        }
    }
}
