package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView publicHomePage(ModelAndView modelAndView){
        modelAndView.setViewName("public/home");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/home")
    public ModelAndView privateHomePage(ModelAndView modelAndView){
        modelAndView.setViewName("public/home");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView loginPage(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login-error")
    public String loginPageError(Model model){
        model.addAttribute("login-error", "true");
        return "login";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/register")
    public ModelAndView registerPage(ModelAndView modelAndView){
        modelAndView.setViewName("register");
        return modelAndView;
    }
}
