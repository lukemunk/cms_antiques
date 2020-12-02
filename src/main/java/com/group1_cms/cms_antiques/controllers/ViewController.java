package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.components.PasswordResetFormValidator;
import com.group1_cms.cms_antiques.components.RegistrationFormValidator;
import com.group1_cms.cms_antiques.components.UserProfileFormValidator;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.models.UserPasswordDto;
import com.group1_cms.cms_antiques.models.UserProfileDto;
import com.group1_cms.cms_antiques.services.*;
import com.group1_cms.cms_antiques.spring.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.UUID;

@Controller
public class ViewController {

    private RegistrationFormValidator registrationFormValidator;
    private UserService userService;
    private RoleService roleService;
    private PermissionService permissionService;
    private PostsService postsService;
    private StateService stateService;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordResetFormValidator passwordResetFormValidator;
    private UserProfileFormValidator userProfileFormValidator;

    @Autowired
    public ViewController(RegistrationFormValidator registrationFormValidator, PasswordResetFormValidator passwordResetFormValidator,
                          UserProfileFormValidator userProfileFormValidator, UserService userService, RoleService roleService, PermissionService permissionService,
                          StateService stateService, PostsService postsService, JwtTokenProvider jwtTokenProvider){
        this.registrationFormValidator = registrationFormValidator;
        this.passwordResetFormValidator = passwordResetFormValidator;
        this.userProfileFormValidator = userProfileFormValidator;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.stateService = stateService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.postsService = postsService;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView publicHomePage(){
        List<Post> postList = postsService.getPosts("all", "",1);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("posts",postList);
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

    @RequestMapping(method = RequestMethod.GET, value = "/member/accountProfile")
    public ModelAndView editProfile(){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView("member/accountProfile");
        UserProfileDto userProfileDto = userService.getUserWithProfileInfo(currentUser.getUsername());
        modelAndView.addObject("stateList", stateService.findAllStates());
        modelAndView.addObject("userProfileForm", userProfileDto);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/member/accountProfile")
    public ModelAndView saveUserProfile(@ModelAttribute("userProfileForm") UserProfileDto userProfileDto, BindingResult bindingResult){
        User currentUser = userService.getUserFromUserDetails((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        userProfileDto.setOlduserName(currentUser.getUsername());
        userProfileDto.setOldEmail(currentUser.getEmail());

        userProfileFormValidator.validate(userProfileDto, bindingResult);
        if(bindingResult.hasErrors()){
            return new ModelAndView("member/accountProfile").addObject("stateList", stateService.findAllStates());
        }
        userService.saveUserProfile(userProfileDto);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/resetPassword")
    public ModelAndView resetPassword(){
        ModelAndView modelAndView = new ModelAndView("member/resetPassword");
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
