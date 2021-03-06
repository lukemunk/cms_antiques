package com.group1_cms.cms_antiques.controllers;


import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


import java.util.Collection;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //System.out.println("\n\n\n\n\n\n\n"+post.getCreator().getUsername()+"\n"+authentication.getName()+"\n"+authentication.getAuthorities()+"\n\n\n\n\n");


        if (post == null) {
            // Handle no post found
            return new ModelAndView("redirect:/posts/all/1");
        }

        // Redirects them to edit page if they are the creator or an Admin
        if (post.getCreator().getUsername().equals(authentication.getName()) || authentication.getAuthorities().contains("Modify_Posts"))
        {
            newView = new ModelAndView("posts/editpost");
        }

        
        newView.addObject("post", post);
        newView.addObject("username", currentPrincipalName);
        newView.addObject("categories", postsService.getAllCategories());
        newView.addObject("allTags", postsService.getAllTags());
        return newView;
    }

    // Returns a page where they can create a new post
    @RequestMapping("posts/newpost")
    public ModelAndView newPost( Model model)
    {
        Post post = new Post();
        ModelAndView newView = new ModelAndView("posts/newpost");

        newView.addObject("post", post);
        newView.addObject("categories", postsService.getAllCategories());
        newView.addObject("allTags", postsService.getAllTags());
        return newView;
    }

    // Gets new Post, saves it, redirects to where it is at
    @RequestMapping("posts/view/editpost_{id}")
    public ModelAndView postToForums(@PathVariable("id") String id, @ModelAttribute(value="post") Post post)
    {
        try
        {
        	
            ModelAndView newView = new ModelAndView("redirect:/posts/view/" + id);
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            


            if (principal instanceof UserDetails)
            {
                username = ((UserDetails) principal).getUsername();
            } else
            {
                username = principal.toString();
            }

            
           try {
        	   
           
			post.setCreator(postsService.findById(post.getId().toString()).getCreator());
			
          // Checks to make sure this is the post creator
            if (username.equals(post.getCreator().getUsername()) || authorities.contains("Admin_Permissions"))
            {
            	
            	
                if (post.getItem().getId() == null)
                {
                    post.getItem().setId(UUID.randomUUID());
                }

                if (post == null)
                {
                    // Handle no post found
                    return new ModelAndView("redirect:/posts/all/1");
                }
                
                postsService.updatePost(post);
                

                newView.addObject("post", post);
                newView.addObject("categories", postsService.getAllCategories());
            }

            return newView;
           }
           catch(EmptyResultDataAccessException e) {
        	   if (post.getItem().getId() == null)
               {
                   post.getItem().setId(UUID.randomUUID());
               }

               postsService.updatePost(post);

               newView.addObject("post", post);
               newView.addObject("categories", postsService.getAllCategories());
               return newView;
           }
        }
        catch (Exception e)
        {
            ModelAndView newView = new ModelAndView("redirect:/posts");
            return newView;
        }

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
    public ModelAndView posts(){
        // Gets the number of pages

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
        
        

        if(category == null)
            category = "all";

        int currentPage;
        try {
        	currentPage = Integer.parseInt(page);
            if(currentPage<=0)
                currentPage = 1;
        } catch (NumberFormatException nfe) {
            currentPage = 1;
        }

        if (searchIN == null)
        {
            // Do nothing
        }
        else
        {
            search = searchIN;
        }
        
        pages = (int)Math.ceil((double)postsService.getAllPostsCount(category, search) / 10);
        ModelAndView newView = new ModelAndView("public/posts.html");
        
        newView.addObject("page", currentPage);
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
