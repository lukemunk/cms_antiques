package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PostContentController
{
    private PostsService postsService;

    @Autowired
    public void PostsAndClassifiedsController(PostsService postsService)
    {
        this.postsService = postsService;
    }

    @RequestMapping(value="/posts/post")
    public String getPostContent(Model model)
    {
        model.addAttribute("posts", postsService.getPosts());
        return "postForum.html";
    }
}
