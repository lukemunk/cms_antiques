package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

public class StartupDatabaseLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean isInitialized = false;
    private RoleService roleService;
    private PermissionService permissionService;
    private UserService userService;

    @Autowired
    public StartupDatabaseLoader(RoleService roleService, PermissionService permissionService, UserService userService){
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if(isInitialized)
            return; //return if database is already seeded with roles and permissions

        //create user to add as default admin user
        User defaultAdmin = createDefaultAdmin("first", "last", "password", "admin", "admin@mail.com");

        //create roles and save to database
        Role member = createRole("ROLE_Member");
        Role admin = createRole("ROLE_Admin");
        Role moderator = createRole("ROLE_Moderator");

        //create permissions and save to database
        Permission createContent = createPermission("Create_Content");
        Permission viewClassifieds = createPermission("View_Classifieds");
        Permission addRemovePermissions =  createPermission("Add_Remove_Permissions");
        Permission viewRestricted = createPermission("View_Restricted");
        Permission addRemoveRoles = createPermission("Add_Remove_Roles");
        Permission addRemoveUser = createPermission("Add_Remove_User");

        //create permission lists for roles
        List<Permission> memberPermissionList = new ArrayList<>();
        List<Permission> adminPermissionList = new ArrayList<>();
        List<Permission> moderatorPermissionList = new ArrayList<>();

        memberPermissionList.add(createContent);
        memberPermissionList.add(viewClassifieds);

        adminPermissionList.add(createContent);
        adminPermissionList.add(viewClassifieds);
        adminPermissionList.add(addRemovePermissions);
        adminPermissionList.add(viewRestricted);
        adminPermissionList.add(addRemoveRoles);
        adminPermissionList.add(addRemoveUser);

        moderatorPermissionList.add(viewRestricted);
        moderatorPermissionList.add(viewClassifieds);

        //add list of permissions to roles and save to Role_Permission in database
        roleService.addPermissionsToRole(member, memberPermissionList);
        roleService.addPermissionsToRole(admin, adminPermissionList);
        roleService.addPermissionsToRole(moderator, moderatorPermissionList);

        userService.saveDefaultAdminUser(defaultAdmin);

        isInitialized = true;
    }

    private Role createRole(String name){
        Role role = new Role(name);
        roleService.saveRole(role);
        return role;
    }

    private Permission createPermission(String name){
        Permission permission = new Permission(name);
        permissionService.savePermission(permission);
        return permission;
    }

    private User createDefaultAdmin(String fName, String lName, String password, String username, String email){
        User admin = new User();
        admin.setFirstName(fName);
        admin.setLastName(lName);
        admin.setPassword(password);
        admin.setUsername(username);
        admin.setEmail(email);
        return admin;
    }
}
