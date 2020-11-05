package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/")
public class PostContentController
{
    private PostsService postsService;

    @Autowired
    public PostContentController(PostsService postsService)
    {
        this.postsService = postsService;
    }

    // Returns a post Page
    @RequestMapping("posts/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Post post = postsService.findById(id);
        if (post == null) {
            // Handle no post found
            return "redirect:/";
        }
        model.addAttribute("post", post);
        return "posts/view";
    }

    // Gets new Post, saves it, redirects to where it is at
    @RequestMapping("postToForums/{id}")
    public String postToForums(@PathVariable("id") Long id, Model model)
    {
        Post post = (Post)model;
        if (post == null) {
            // Handle no post found
            return "redirect:/";
        }

        postsService.getPosts().add(post);

        model.addAttribute("post", post);
        return "posts/view";
    }

    //region Post List
    @GetMapping(path = "posts/{Category}",
            produces = "application/json")
    public String getPostsCategory(Model model, @PathVariable String Category)
    {
        model.addAttribute("posts", postsService.getPostsFromCategory(Category));
        return "public/posts.html";
    }

    @GetMapping(path = "posts/",
            produces = "application/json")
    public String getAllPosts(Model model,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size)
    {	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        

        // Gets posts in limited order
        List<Post> tenPosts = postsService.getPosts().stream().limit(10).collect(Collectors.toList());
        model.addAttribute("tenPosts", tenPosts);
        return "index";
    }
    //endregion

}
