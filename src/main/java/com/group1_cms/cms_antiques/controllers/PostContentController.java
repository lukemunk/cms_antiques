package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/posts/")
public class PostContentController
{
    private PostsService postsService;

    @Autowired
    public PostContentController(PostsService postsService)
    {
        this.postsService = postsService;
    }

    // Returns a post Page
    @RequestMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Post post = postsService.findById(id);
        if (post == null) {
            // Handle no post found
            return "redirect:/";
        }
        model.addAttribute("post", post);
        return "posts/view";
    }

    //region Post List
    @GetMapping(path = "/{Category}",
            produces = "application/json")
    public String getPostsCategory(Model model, @PathVariable String Category)
    {
        model.addAttribute("posts", postsService.getPostsFromCategory(Category));
        return "public/posts.html";
    }

    @GetMapping(path = "/",
            produces = "application/json")
    public String getAllPosts(Model model)
    {
        // Gets posts in limited order
        List<Post> tenPosts = postsService.getPosts().stream().limit(10).collect(Collectors.toList());
        model.addAttribute("tenPosts", tenPosts);
        return "index";
    }
    //endregion

}
