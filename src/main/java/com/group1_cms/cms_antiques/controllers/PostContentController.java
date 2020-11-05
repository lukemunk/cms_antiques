package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/")
public class PostContentController
{
    private PostsService postsService;
    private long postCount = 0;

    @Autowired
    public PostContentController(PostsService postsService)
    {
        this.postsService = postsService;
        postCount = postsService.getAllPosts().stream().count();
    }

    // Returns a post Page
    @RequestMapping("posts/view/{id}")
    public ModelAndView view(@PathVariable("id") String id, Model model)
    {
        Post post = postsService.findById(id);
        ModelAndView newView = new ModelAndView("posts/view");

        if (post == null) {
            // Handle no post found
            return new ModelAndView("posts");
        }
        newView.addObject("post", post);
        return newView;
    }

    // Gets new Post, saves it, redirects to where it is at
    @RequestMapping("posts/view/editpost_{id}")
    public ModelAndView postToForums(@PathVariable("id") String id, @ModelAttribute(value="post") Post post)
    {
        ModelAndView newView = new ModelAndView("posts/view/" + id);

        if (post == null) {
            // Handle no post found
            return new ModelAndView("posts");
        }
        if (postsService.getPosts().contains(post))
        {
            postsService.updatePost(post);
        }

        postsService.getPosts().add(post);

        newView.addObject("post", post);
        return newView;
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
    {
        // Gets the number of pages
        long pages = (long)Math.ceil((double)postCount / 10);

        // Gets posts in limited order
        List<Post> tenPosts = postsService.getPosts().stream().limit(10).collect(Collectors.toList());
        model.addAttribute("tenPosts", tenPosts);
        return "index";
    }
    //endregion

}
