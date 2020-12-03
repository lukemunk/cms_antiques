package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.PasswordResetFormValidator;
import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.components.UserProfileFormValidator;
import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.services.*;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import com.sun.source.tree.ModuleTree;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ViewControllerTest
{
    private ViewController viewController;

    private RegistrationFormValidator registrationFormValidator;
    private PasswordResetFormValidator passwordResetFormValidator;
    private UserProfileFormValidator userProfileFormValidator;
    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;
    private StateService stateService;
    private PostsService postsService;
    private JwtTokenProvider jwtTokenProvider;

    //region GET tests
    @Test
    void publicHomePage() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        viewController.publicHomePage();
    }

    @Test
    void loginPage() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        viewController.loginPage();
    }

    @Test
    void loginPageError() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        viewController.loginPageError();
    }

    @Test
    void registerPage() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        viewController.registerPage();
    }

    @Test
    void registerUser() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        User newUser = new User();
        newUser.setUsername("Tron");
        BindingResult newBinding = Mockito.mock(BindingResult.class);
        HttpServletResponse newResponse = Mockito.mock(HttpServletResponse.class);

        viewController.registerUser(newUser, newBinding, newResponse);
    }

    @WithMockUser(value = "admin")
    @Test
    void editProfile() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        List<StateDto> newStateDTOList = new ArrayList<>();
        UserDetails newDeets = Mockito.mock(UserDetails.class);
        UserProfileDto newUserDTO = Mockito.mock(UserProfileDto.class);
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.when(newContext.getAuthentication().getPrincipal()).thenReturn(newDeets);
        Mockito.when(newDeets.getUsername()).thenReturn("Tron");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        Mockito.when(stateService.findAllStates()).thenReturn(newStateDTOList);
        SecurityContextHolder.setContext(newContext);
        Mockito.when(userService.getUserWithProfileInfo(newDeets.getUsername())).thenReturn(newUserDTO);
        viewController.editProfile();
    }

    @Test
    void resetPassword() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        viewController.resetPassword();
    }
    //endregion

    //region POST tests
    @Test
    void saveUserProfile() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        UserProfileDto newUserDTO = Mockito.mock(UserProfileDto.class);
        BindingResult newBinding = Mockito.mock(BindingResult.class);
        UserDetails newDeets = Mockito.mock(UserDetails.class);
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.when(newContext.getAuthentication().getPrincipal()).thenReturn(newDeets);
        Mockito.when(newDeets.getUsername()).thenReturn("Tron");
        User newUser = new User();
        newUser.setUsername("Tron");
        Mockito.when(userService.getUserFromUserDetails(newDeets)).thenReturn(newUser);

        //viewController.saveUserProfile(newUserDTO, newBinding);
    }

    @Test
    void saveResetPassword() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        //viewController.saveResetPassword();
    }

    @WithMockUser("admin")
    @Test
    void closeAccount() throws Exception
    {
        // Mocks our services
        registrationFormValidator = Mockito.mock(RegistrationFormValidator.class);
        passwordResetFormValidator = Mockito.mock(PasswordResetFormValidator.class);
        userProfileFormValidator = Mockito.mock(UserProfileFormValidator.class);
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        permissionService = Mockito.mock(PermissionService.class);
        stateService = Mockito.mock(StateService.class);
        postsService = Mockito.mock(PostsService.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        viewController = new ViewController(registrationFormValidator, passwordResetFormValidator, userProfileFormValidator,
                userService, roleService, permissionService, stateService, postsService, jwtTokenProvider);

        //viewController.closeAccount();
    }
    //endregion
}