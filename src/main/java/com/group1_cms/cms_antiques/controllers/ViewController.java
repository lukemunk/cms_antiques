package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.models.Permission;
import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ViewController {

    private RegistrationFormValidator registrationFormValidator;
    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;

    @Autowired
    public ViewController(RegistrationFormValidator registrationFormValidator, UserService userService,
                          RoleService roleService, PermissionService permissionService){
        this.registrationFormValidator = registrationFormValidator;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView publicHomePage(){
        return new ModelAndView("index");
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
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/register")
    public ModelAndView registerPage(){
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ModelAndView registerUser(@ModelAttribute ("user") User newUser, BindingResult bindingResult){

        registrationFormValidator.validate(newUser, bindingResult);

        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }
        userService.saveNewUser(newUser);

        return new ModelAndView("login");
    }

}
