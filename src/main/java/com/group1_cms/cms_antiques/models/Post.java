package com.group1_cms.cms_antiques.models;

import java.util.UUID;

public class Post {
	private String id;
	private String title;
	private String story;
	private Item item;
	private User creator;
	
	public Post() {
		title = "";
		story = "";
		id = UUID.randomUUID().toString();
		item = new Item();
	}
	
	public Post(String title, String story, Item item, User creator) {
		id = UUID.randomUUID().toString();
		this.title = title;
		this.story = story;
		this.item = item;
		this.creator = creator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Override
	public boolean equals(Object obj)
	{
		Post that = (Post)obj;
		if (this.getId().equals( that.getId()) )
		{
			return true;
		}

		return false;
	}
}
