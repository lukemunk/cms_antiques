package com.group1_cms.cms_antiques.models;

import java.util.List;
import java.util.UUID;

public class Post {
	private UUID id;
	private String title;
	private String story;
	private Item item;
	private User creator;
	private List<String> tags;
	
	public Post() {
		title = "";
		story = "";
		id = UUID.randomUUID();
		item = new Item();
	}
	
	public Post(String title, String story, Item item, User creator) {
		id = UUID.randomUUID();
		this.title = title;
		this.story = story;
		this.item = item;
		this.creator = creator;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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
