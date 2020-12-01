package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.State;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.StateService;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StartupDatabaseLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean isInitialized = false;
    private RoleService roleService;
    private PermissionService permissionService;
    private UserService userService;
    private StateService stateService;

    @Autowired
    public StartupDatabaseLoader(RoleService roleService, PermissionService permissionService, UserService userService,
                                 StateService stateService){
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
        this.stateService = stateService;
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

        //userService.saveDefaultAdminUser(defaultAdmin);

        List<State> stateList = new ArrayList<>();
        addStatesToList(stateList);
        for (State state: stateList) {
            stateService.saveState(state);
        }

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

    private void addStatesToList(List<State> states){

        states.add(new State("Alabama"));
        states.add(new State("Alaska"));
        states.add(new State("Arizona"));
        states.add(new State("Arkansas"));
        states.add(new State("California"));
        states.add(new State("Colorado"));
        states.add(new State("Connecticut"));
        states.add(new State("Delaware"));
        states.add(new State("District of Columbia"));
        states.add(new State("Florida"));
        states.add(new State("Georgia"));
        states.add(new State("Hawaii"));
        states.add(new State("Idaho"));
        states.add(new State("Illinois"));
        states.add(new State("Indiana"));
        states.add(new State("Iowa"));
        states.add(new State("Kansas"));
        states.add(new State("Kentucky"));
        states.add(new State("Louisiana"));
        states.add(new State("Maine"));
        states.add(new State("Maryland"));
        states.add(new State("Massachusetts"));
        states.add(new State("Michigan"));
        states.add(new State("Minnesota"));
        states.add(new State("Mississippi"));
        states.add(new State("Missouri"));
        states.add(new State("Montana"));
        states.add(new State("Nebraska"));
        states.add(new State("Nevada"));
        states.add(new State("New Hampshire"));
        states.add(new State("New Jersey"));
        states.add(new State("New Mexico"));
        states.add(new State("New York"));
        states.add(new State("North Carolina"));
        states.add(new State("North Dakota"));
        states.add(new State("Ohio"));
        states.add(new State("Oklahoma"));
        states.add(new State("Oregon"));
        states.add(new State("Pennsylvania"));
        states.add(new State("Rhode Island"));
        states.add(new State("South Carolina"));
        states.add(new State("South Dakota"));
        states.add(new State("Tennessee"));
        states.add(new State("Texas"));
        states.add(new State("Utah"));
        states.add(new State("Vermont"));
        states.add(new State("Virginia"));
        states.add(new State("Washington"));
        states.add(new State("West Virginia"));
        states.add(new State("Wisconsin"));
        states.add(new State("Wyoming"));
    }
}
