package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.PasswordResetFormValidator;
import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.models.UserPasswordDto;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.UserService;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class ViewController {

    private RegistrationFormValidator registrationFormValidator;
    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordResetFormValidator passwordResetFormValidator;

    @Autowired
    public ViewController(RegistrationFormValidator registrationFormValidator, PasswordResetFormValidator passwordResetFormValidator,
                          UserService userService, RoleService roleService, PermissionService permissionService,
                          JwtTokenProvider jwtTokenProvider){
        this.registrationFormValidator = registrationFormValidator;
        this.passwordResetFormValidator = passwordResetFormValidator;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView publicHomePage(){
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView loginPage(){
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login-error")
    public ModelAndView loginPageError(){
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginError", true);
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView registerPage(){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ModelAndView registerUser(@ModelAttribute ("user") User newUser, BindingResult bindingResult, HttpServletResponse response) {

        registrationFormValidator.validate(newUser, bindingResult);

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.saveNewUser(newUser);
        jwtTokenProvider.autoLogin(newUser.getUsername(), newUser.getPasswordConfirm(), response);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/accountProfile/{id}")
    public ModelAndView editProfile(@PathVariable UUID id){
        ModelAndView modelAndView = new ModelAndView("member/accountProfile");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/resetPassword")
    public ModelAndView resetPassword(){
        ModelAndView modelAndView = new ModelAndView("/member/resetPassword");
        modelAndView.addObject("passwordResetForm", new UserPasswordDto());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/resetPassword")
    public ModelAndView saveResetPassword(@ModelAttribute ("passwordResetForm") UserPasswordDto userPasswordDto, BindingResult bindingResult){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userPasswordDto.setUserName(currentUser.getUsername());
        userPasswordDto.setEncryptedCurrentPassword(currentUser.getPassword());
        passwordResetFormValidator.validate(userPasswordDto, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("member/resetPassword");
        }
        userService.resetUserPassword(userPasswordDto);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/closeAccount/{id}")
    public ModelAndView closeAccount(@PathVariable UUID id){
        ModelAndView modelAndView = new ModelAndView("member/closeAccount");
        return modelAndView;
    }
}
