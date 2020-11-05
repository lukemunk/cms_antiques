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

import java.time.ZonedDateTime;
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

        //create roles
        Role admin = new Role("ROLE_Admin");
        admin.setCreatedOn(ZonedDateTime.now());

        Role member = new Role("ROLE_Member");
        member.setCreatedOn(ZonedDateTime.now());

        Role moderator = new Role("ROLE_Moderator");
        moderator.setCreatedOn(ZonedDateTime.now());

        //create permissions
        Permission createContent = new Permission("Create_Content");
        createContent.setCreatedOn(ZonedDateTime.now());

        Permission viewClassifieds = new Permission("View_Classifieds");
        viewClassifieds.setCreatedOn(ZonedDateTime.now());

        Permission modifyUserPermissions = new Permission("Modify_User_Permissions");
        modifyUserPermissions.setCreatedOn(ZonedDateTime.now());

        Permission viewRestricted = new Permission("View_Restricted");
        viewRestricted.setCreatedOn(ZonedDateTime.now());

        Permission modifyUserRoles = new Permission("Modify_User_Roles");
        modifyUserRoles.setCreatedOn(ZonedDateTime.now());

        Permission modifyUser = new Permission("Modify_User");
        modifyUser.setCreatedOn(ZonedDateTime.now());

        Permission adminPermissions = new Permission("Admin_Permissions");
        adminPermissions.setCreatedOn(ZonedDateTime.now());

        Permission modifyClassifieds = new Permission("Modify_Classifieds");
        modifyClassifieds.setCreatedOn(ZonedDateTime.now());

        Permission modifyPosts = new Permission("Modify_Posts");
        modifyPosts.setCreatedOn(ZonedDateTime.now());

        Permission modifyCategory = new Permission("Modify_Category");
        modifyCategory.setCreatedOn(ZonedDateTime.now());



        //create permissions lists by role
        List<Permission> memberPermissionList = new ArrayList<>();
        List<Permission> adminPermissionList = new ArrayList<>();
        List<Permission> moderatorPermissionList = new ArrayList<>();

        memberPermissionList.add(createContent);
        memberPermissionList.add(viewClassifieds);

        adminPermissionList.add(adminPermissions);
        adminPermissionList.add(createContent);
        adminPermissionList.add(viewClassifieds);
        adminPermissionList.add(modifyUserPermissions);
        adminPermissionList.add(viewRestricted);
        adminPermissionList.add(modifyUserRoles);
        adminPermissionList.add(modifyUser);
        adminPermissionList.add(modifyClassifieds);
        adminPermissionList.add(modifyPosts);

        moderatorPermissionList.add(adminPermissions);
        moderatorPermissionList.add(viewRestricted);
        moderatorPermissionList.add(viewClassifieds);


        //roleService.addPermissionsToRole adds any roles and associated permissions to database
        //if role, permissions, or role_permissions is already in the database then do nothing
        roleService.addPermissionsToRole(member, memberPermissionList);
        roleService.addPermissionsToRole(admin, adminPermissionList);
        roleService.addPermissionsToRole(moderator, moderatorPermissionList);

        userService.saveDefaultAdminUser(defaultAdmin);

        isInitialized = true;
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
