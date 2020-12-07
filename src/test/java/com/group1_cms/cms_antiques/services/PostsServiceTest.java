package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.repositories.PostsRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class PostsServiceTest
{

    private PostsRepository postsRepository;
    private PostsService postsService;

    @Test
    void getPosts()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        List<Post> newList = new ArrayList<>();
        Mockito.when(postsRepository.getPosts("", "", 1, 10)).thenReturn(newList);
        Assert.assertEquals(newList,postsService.getPosts("", "", 1));
    }

    @Test
    void getAllPostsCount()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        Mockito.when(postsRepository.getAllPostsCount("", "")).thenReturn(10);
        Assert.assertEquals(10,postsService.getAllPostsCount("", ""));
    }

    @Test
    void findById()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        Post newPost = new Post();
        Mockito.when(postsRepository.getPostByID("10")).thenReturn(newPost);
        Assert.assertEquals(newPost,postsService.findById("10"));
    }

    @Test
    void updatePost()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        Post newPost = new Post();

        postsService.updatePost(newPost);
    }

    @Test
    void deletePost()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        Post newPost = new Post();

        postsService.deletePost(newPost);
    }

    @Test
    void getAllCategories()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        List<String> newList = new ArrayList<>();
        Mockito.when(postsRepository.getAllCategories()).thenReturn(newList);
        Assert.assertEquals(newList,postsService.getAllCategories());
    }

    @Test
    void getAllTags()
    {
        postsRepository = Mockito.mock(PostsRepository.class);
        postsService = new PostsService(postsRepository);
        List<String> newList = new ArrayList<>();
        Mockito.when(postsRepository.getAllTags()).thenReturn(newList);
        Assert.assertEquals(newList,postsService.getAllTags());
    }

}