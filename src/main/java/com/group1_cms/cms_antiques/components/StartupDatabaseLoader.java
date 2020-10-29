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

        Permission addRemovePermissions = new Permission("Add_Remove_Permissions");
        addRemovePermissions.setCreatedOn(ZonedDateTime.now());

        Permission viewRestricted = new Permission("View_Restricted");
        viewRestricted.setCreatedOn(ZonedDateTime.now());

        Permission addRemoveRoles = new Permission("Add_Remove_Roles");
        addRemoveRoles.setCreatedOn(ZonedDateTime.now());

        Permission addRemoveUser = new Permission("Add_Remove_User");
        addRemoveUser.setCreatedOn(ZonedDateTime.now());

        //create permissions lists by role
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
