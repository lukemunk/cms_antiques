package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.PermissionService;
import com.group1_cms.cms_antiques.services.RoleService;
import com.group1_cms.cms_antiques.services.UserService;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ViewController {

    private RegistrationFormValidator registrationFormValidator;
    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ViewController(RegistrationFormValidator registrationFormValidator, UserService userService,
                          RoleService roleService, PermissionService permissionService, JwtTokenProvider jwtTokenProvider){
        this.registrationFormValidator = registrationFormValidator;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView publicHomePage(){
        return new ModelAndView("home");
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
}
