package com.group1_cms.cms_antiques.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.services.PostsService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(PostContentController.class)
@AutoConfigureMockMvc
public class PostContentControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostContentController postContentController;

    @MockBean
    private PostsService postsService;

    private static ObjectMapper mapper = new ObjectMapper();

    //region GET tests
    @WithMockUser(value = "admin")
    @Test
    public void getView() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        posts.add(newPost);
        // Makes sure we receive the right post
        Mockito.when(postsService.getPosts("", "", 1)).thenReturn(posts);
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/view/" + newRandom)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void postMainPage() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        List<Post> posts = new ArrayList<>();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        posts.add(newPost);
        // Makes sure we receive the right post
        Mockito.when(postsService.getPosts("", "", 1)).thenReturn(posts);
        Mockito.when(postContentController.posts()).thenReturn(new ModelAndView("redirect:/posts/all/1"));
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrlTemplate("/posts/all/1"));

    }
    //endregion

    //region POST tests
    @WithMockUser(value = "admin")
    @Test
    public void newPostView() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        // Now tests to make sure the file is there, if so it'll return a 200 Code instead of a 404
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/newpost", newPost)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void newPostCreation() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        Mockito.when(postContentController.postToForums(newRandom.toString(), newPost)).thenReturn(new ModelAndView("redirect:/posts/view/" + newRandom));
        // Now tests to make sure the file is there, if so it'll return a 302 redirection to the right page
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/view/editpost_" + newRandom, newPost)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrlTemplate("/posts/view/" + newRandom));

    }

    @WithMockUser(value = "admin")
    @Test
    public void updatePost() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        Mockito.when(postContentController.postToForums(newRandom.toString(), newPost)).thenReturn(new ModelAndView("redirect:/posts/view/" + newRandom));
        // Now tests to make sure the file is there, if so it'll return a 302 redirection to the right page
        // This will first create then update
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/view/editpost_" + newRandom, newPost)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrlTemplate("/posts/view/" + newRandom));
        // This is the updating of the post
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/view/editpost_" + newRandom, newPost)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrlTemplate("/posts/view/" + newRandom));
    }

    @WithMockUser(value = "admin")
    @Test
    public void postDeletion() throws Exception
    {
        // Mocks our services
        postsService = Mockito.mock(PostsService.class);
        postContentController = Mockito.mock(PostContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(postContentController).build();
        // Creates new Post
        UUID newRandom = UUID.randomUUID();
        Post newPost = new Post();
        newPost.setId(newRandom);
        newPost.setTitle("TestPost");
        Mockito.when(postContentController.deletePost(newRandom.toString(), newPost)).thenReturn(new ModelAndView("redirect:/posts/all/1"));
        // Now tests to make sure the file is there, if so it'll return a 302 redirection to the right page
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/view/deletepost_" + newRandom, newPost)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrlTemplate("/posts/all/1"));

    }
    //endregion

}