package com.group1_cms.cms_antiques.Posts;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
	
	private PostsRepository postsRepository;
	
	@Autowired
	public PostsService(PostsRepository postsRepository) {
		this.postsRepository = postsRepository;
		
	}
	
	public ArrayList<Post> getPosts(){
		return postsRepository.getPosts(10);
	}
	
	public ArrayList<Post> getPostsFromCategory(String category){
		return postsRepository.getPostsFromCategory(10, category);
	}

}
