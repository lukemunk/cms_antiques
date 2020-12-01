package com.group1_cms.cms_antiques.services;

import java.awt.print.Book;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.repositories.PostsRepository;

@Service
public class PostsService {
	
	private PostsRepository postsRepository;
	
	@Autowired
	public PostsService(PostsRepository postsRepository) {
		this.postsRepository = postsRepository;
		
	}
	
	public List<Post> getPosts(String category, String search, int page){
		return postsRepository.getPosts(category, search, page, 10);
	}

	public int getAllPostsCount(String category, String search){
		return postsRepository.getAllPostsCount(category, search);
	}

	public Post findById(String id)
	{
		Post newPost = postsRepository.getPostByID(id);
		return newPost;
	}

	public void updatePost(Post postIN)
	{
		postsRepository.updatePost(postIN);
	}

    public void deletePost(Post post)
    {
    	postsRepository.deletePost(post);
    }

	public List<String> getAllCategories()
	{
		return postsRepository.getAllCategories();
	}
	
	public List<String> getAllTags()
	{
		return postsRepository.getAllTags();
	}
}
