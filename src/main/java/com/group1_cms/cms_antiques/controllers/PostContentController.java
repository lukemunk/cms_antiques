package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/posts/post")
public class PostContentController
{
    private PostsService postsService;
    private List<Post> currentPosts;
    private Post currentPost;

    @Autowired
    public void PostsAndClassifiedsController(PostsService postsService)
    {
        this.postsService = postsService;
    }

    // Returns a post Page
    @RequestMapping(value="/")
    public String getPostPage(Model model)
    {
        model.addAttribute("posts", postsService.getPosts());
        return "postForum.html";
    }

    //region Post Content
    @GetMapping(path = "/",
                produces = "application/json")
    public String getTitle()
    {
        return currentPost.getTitle();
    }

    @GetMapping(path = "/",
            produces = "application/json")
    public String getStory()
    {
        return currentPost.getStory();
    }

    @GetMapping(path = "/",
            produces = "application/json")
    public Item getItem()
    {
        return currentPost.getItem();
    }
    //endregion

}
