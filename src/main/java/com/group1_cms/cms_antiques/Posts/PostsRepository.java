package com.group1_cms.cms_antiques.Posts;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.group1_cms.cms_antiques.Item.Item;

@Repository
public class PostsRepository {
	private ArrayList<Post> posts;

	public PostsRepository() {
		posts = new ArrayList<Post>();
		posts.add(new Post("Post Title", "", new Item("Doll", "Collectible")));
		posts.add(new Post("Post Title", "", new Item("Chair", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Couch", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Painting", "Art")));
		posts.add(new Post("Post Title", "", new Item("Ring", "Jewelry")));
		posts.add(new Post("Post Title", "", new Item("Necklace", "Jewelry")));
		posts.add(new Post("Post Title", "", new Item("Toy Car", "Collectible")));
		posts.add(new Post("Post Title", "", new Item("Dress", "Clothing")));
		posts.add(new Post("Post Title", "", new Item("Pants", "Clothing")));
		posts.add(new Post("Post Title", "", new Item("Hat", "Clothing")));
		posts.add(new Post("Post Title", "", new Item("Shirt", "Clothing")));
		posts.add(new Post("Post Title", "", new Item("Table", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Stool", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Pottery", "Art")));
		posts.add(new Post("Post Title", "", new Item("Socks", "Clothing")));
		posts.add(new Post("Post Title", "", new Item("Crown", "Jewelry")));
		posts.add(new Post("Post Title", "", new Item("Painting", "Art")));
		posts.add(new Post("Post Title", "", new Item("Stand", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Bed", "Furniture")));
		posts.add(new Post("Post Title", "", new Item("Car", "Collectible")));
		posts.add(new Post("Post Title", "", new Item("Painting 1", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 2", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 3", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 4", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 5", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 6", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 7", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 8", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 9", "Art")));
		posts.add(new Post("Post Title", "", new Item("Painting 10", "Art")));	
	}
	
	public ArrayList<Post> getPosts(int numberOfPosts){
		ArrayList<Post> posts = new ArrayList<Post>();
		this.posts.stream()
		.limit(10)
		.forEachOrdered(posts::add);
		return posts;
	}
	
public ArrayList<Post> getPostsFromCategory(int numberOfPosts, String category){
		
		ArrayList<Post> posts = new ArrayList<Post>();
		this.posts.stream()
		.filter(post -> post.getItem().getCategory().equalsIgnoreCase(category))
		.limit(10)
		.forEachOrdered(posts::add);
		
		return posts;
	}
}
