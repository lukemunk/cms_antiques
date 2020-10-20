package com.group1_cms.cms_antiques.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group1_cms.cms_antiques.services.PostsService;

@Controller
public class PostsAndClassifiedsController {
	
	private PostsService postsService;

	@Autowired
	public PostsAndClassifiedsController(PostsService postsService) {
		this.postsService = postsService;
	}
	
	@RequestMapping(value="/posts")
	public String posts(Model model){
		model.addAttribute("posts", postsService.getPosts());
		return "public/posts.html";
	}
	
	@RequestMapping(value="/posts/{category}")
	public String category(Model model, @PathVariable String category) {
		model.addAttribute("posts", postsService.getPostsFromCategory(category));
		return "public/posts.html";
	}
	
	@RequestMapping(value="/public/classifieds")
	public String classifieds() {
		return "public/posts.html";
	}
}
