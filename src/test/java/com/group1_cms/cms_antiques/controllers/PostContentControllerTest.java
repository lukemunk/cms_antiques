package com.group1_cms.cms_antiques.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.PostsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RunWith(MockitoJUnitRunner.class)
public class PostContentControllerTest
{

    @MockBean
    private PostsService postsService;

    private PostContentController postContentController;

    private static ObjectMapper mapper = new ObjectMapper();

    //region GET tests
    @WithMockUser(value = "admin")
    @Test
    public void getView() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Bob Loblaw");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        posts.add(newPost);
        // Makes sure we receive the right post
        Mockito.when(postsService.findById(newRandom.toString())).thenReturn(newPost);
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        Model newModel = Mockito.mock(Model.class);
        newModel.addAttribute("post", newPost);
        ModelAndView newView = postContentController.view(newRandom.toString(), newModel);

        Assert.assertEquals(newView.getViewName(), "posts/editpost");
    }

    @WithMockUser(value = "admin")
    @Test
    public void getViewNonUser() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Tron");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        posts.add(newPost);
        // Makes sure we receive the right post
        Mockito.when(postsService.findById(newRandom.toString())).thenReturn(newPost);
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        Model newModel = Mockito.mock(Model.class);
        newModel.addAttribute("post", newPost);
        ModelAndView newView = postContentController.view(newRandom.toString(), newModel);

        Assert.assertEquals(newView.getViewName(), "posts/view");
    }

    @WithMockUser(value = "admin")
    @Test
    public void getViewNoPost() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.lenient().when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Tron");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        posts.add(newPost);
        // Makes sure we receive the right post
        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.view(newRandom.toString(), newModel);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts/all/1");
    }

    @WithMockUser(value = "admin")
    @Test
    public void postMainPage() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        posts.add(newPost);
        // Makes sure we receive the right post
        //Mockito.when(postsService.getPosts("", "", 1)).thenReturn(posts);
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        ModelAndView newView = postContentController.posts();

        Assert.assertEquals(newView.getViewName(), "redirect:posts/all/1");
    }

    @WithMockUser(value = "admin")
    @Test
    public void postMainPage2() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        List<String> newCats = new ArrayList<>();
        newCats.add("Cars");
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        posts.add(newPost);
        // Makes sure we receive the right post
        Mockito.lenient().when(postsService.getPosts("", "", 1)).thenReturn(posts);
        Mockito.lenient().when(postsService.getAllPostsCount("", "")).thenReturn(15);
        Mockito.lenient().when(postsService.getAllCategories()).thenReturn(newCats);
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.getAllPosts(newModel, null, null, "1");

        Assert.assertEquals(newView.getViewName(), "public/posts.html");
    }
    //endregion

    //region POST tests
    @WithMockUser(value = "admin")
    @Test
    public void newPostView() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");

        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.newPost(newModel);

        Assert.assertEquals(newView.getViewName(), "posts/newpost");
    }

    @WithMockUser(value = "admin")
    @Test
    public void newPostCreation() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.lenient().when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newContext.getAuthentication().getPrincipal()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Bob Loblaw");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        Mockito.when(postsService.findById(newRandom.toString())).thenReturn(newPost);
        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.postToForums(newRandom.toString(), newPost);


        Assert.assertEquals(newView.getViewName(), "redirect:/posts/view/"+newRandom);
    }

    @WithMockUser(value = "admin")
    @Test
    public void newPostCreationInvalidPost() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.lenient().when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newContext.getAuthentication().getPrincipal()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Bob Loblaw");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        Mockito.lenient().when(postsService.findById(newRandom.toString())).thenReturn(newPost);
        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.postToForums(newRandom.toString(), null);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts");
    }

    @WithMockUser(value = "admin")
    @Test
    public void newPostCreationInvalidUser() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.lenient().when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newContext.getAuthentication().getPrincipal()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Tron");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);

        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.postToForums(newRandom.toString(), newPost);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts");
    }

    @WithMockUser(value = "admin")
    @Test
    public void newPostCreationNoUser() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        SecurityContext newContext = Mockito.mock(SecurityContext.class);
        Authentication newAuth = Mockito.mock(Authentication.class);
        Mockito.when(newContext.getAuthentication()).thenReturn(newAuth);
        Mockito.lenient().when(newContext.getAuthentication().getName()).thenReturn("Bob Loblaw");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        SecurityContextHolder.setContext(newContext);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Tron");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);

        Model newModel = Mockito.mock(Model.class);
        ModelAndView newView = postContentController.postToForums(newRandom.toString(), newPost);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts");
    }

    @WithMockUser(value = "admin")
    @Test
    public void updatePost() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        User newUser = new User();
        // I fight for the users!
        newUser.setUsername("Bob Loblaw");
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        newPost.setCreator(newUser);
        Mockito.when(postsService.findById(newRandom.toString())).thenReturn(newPost);
        Model newModel = Mockito.mock(Model.class);
        postContentController.postToForums(newRandom.toString(), newPost);
        // Now update
        ModelAndView newView = postContentController.postToForums(newRandom.toString(), newPost);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts/view/"+newRandom);
    }

    @WithMockUser(value = "admin")
    @Test
    public void postDeletion() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = new PostContentController(postsService);
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");

        Model newModel = Mockito.mock(Model.class);
        postContentController.postToForums(newRandom.toString(), newPost);
        // Now update
        ModelAndView newView = postContentController.deletePost(newRandom.toString(), newPost);

        Assert.assertEquals(newView.getViewName(), "redirect:/posts/all/1");
    }
    //endregion

}