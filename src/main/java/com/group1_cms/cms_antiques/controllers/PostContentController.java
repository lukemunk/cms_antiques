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
import java.util.UUID;
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
        postCount = postsService.getAllPostsCount("All", "");
    }

    // Returns a post Page where they can also edit the post
    @RequestMapping("posts/view/{id}")
    public ModelAndView view(@PathVariable("id") String id, Model model)
    {
        Post post = postsService.findById(id);
        ModelAndView newView = new ModelAndView("posts/view");

        if (post == null) {
            // Handle no post found
            return new ModelAndView("redirect:/posts/all/1");
        }
        newView.addObject("post", post);
        return newView;
    }

    // Returns a page where they can create a new post
    @RequestMapping("posts/newpost")
    public ModelAndView newPost( Model model)
    {
        Post post = new Post();
        ModelAndView newView = new ModelAndView("posts/newpost");

        newView.addObject("post", post);
        return newView;
    }

    // Gets new Post, saves it, redirects to where it is at
    @RequestMapping("posts/view/editpost_{id}")
    public ModelAndView postToForums(@PathVariable("id") String id, @ModelAttribute(value="post") Post post)
    {
        ModelAndView newView = new ModelAndView("redirect:/posts/view/" + id);
        if (post.getItem().getId() == null)
        {
            post.getItem().setId(UUID.randomUUID());
        }

        if (post == null) {
            // Handle no post found
            return new ModelAndView("redirect:/posts/all/1");
        }
        postsService.updatePost(post);


        newView.addObject("post", post);
        return newView;
    }

    // Gets new Post, saves it, redirects to where it is at
    @RequestMapping("posts/view/deletepost_{id}")
    public ModelAndView deletePost(@PathVariable("id") String id, @ModelAttribute(value="post") Post post)
    {
        ModelAndView newView = new ModelAndView("redirect:/posts/all/1");

        if (post == null) {
            // Handle no post found
            return new ModelAndView("redirect:/posts/all/1");
        }
        postsService.deletePost(post);

        return newView;
    }

    @RequestMapping(value="/posts")
    public ModelAndView posts(Model model){
        // Gets the number of pages
        int pages = (int)Math.ceil((double)postsService.getAllPostsCount("all", "") / 10);

            ModelAndView newView = new ModelAndView("redirect:posts/all/1");

        return newView;
    }

    @GetMapping(path = "posts/{category}/{page}",
            produces = "application/json")
    public ModelAndView getAllPosts(Model model,
                              @RequestParam(required = false) String searchIN,
                              @PathVariable(required = false) String category,
                              @PathVariable String page)
    {
        int pages = 0;
        String search = "";
        // Gets the number of pages
        pages = (int)Math.ceil((double)postsService.getAllPostsCount(category, searchIN) / 10);

        if (searchIN == null)
        {
            // Do nothing
        }
        else
        {
            search = searchIN;
        }

        ModelAndView newView = new ModelAndView("public/posts.html");

        newView.addObject("page", Integer.parseInt(page));
        newView.addObject("totalPages", pages);
        newView.addObject("search", search);
        newView.addObject("category", category.substring(0, 1).toUpperCase() + category.substring(1));
        newView.addObject("categories", postsService.getAllCategories());

        // Gets posts in limited order
        newView.addObject("posts", postsService.getPosts(category, search, Integer.parseInt(page)));

        return newView;
    }
    //endregion

}
