package com.group1_cms.cms_antiques.Posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PostsAndClassifiedsController {
	
	private PostsService postsService;

	@Autowired
	public PostsAndClassifiedsController(PostsService postsService) {
		this.postsService = postsService;
	}
	
	@RequestMapping(value="/Posts")
	public String posts(Model model){
		model.addAttribute("posts", postsService.getPosts());
		return "Posts.html";
	}
	
	@RequestMapping(value="/Posts/{category}")
	public String category(Model model, @PathVariable String category) {
		model.addAttribute("posts", postsService.getPostsFromCategory(category));
		return "Posts.html";
	}
	
	@RequestMapping(value="/Classifieds")
	public String classifieds() {
		return "Posts.html";
	}
}
