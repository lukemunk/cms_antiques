package com.group1_cms.cms_antiques.models;

import java.util.UUID;

public class Classifieds {

	private String id;
	private String title;
	private String description;
	private String price;
	private Item item;
	private User creator;
	
	public Classifieds() {
		title = "Ad Title";
		description = "Stuff about the item";
		price = "Item Price";
		item = new Item();
	}
	
	public Classifieds(String title, String description, String price, Item item, User creator) {
		id = UUID.randomUUID().toString();
		this.title = title;
		this.description = description;
		this.price = price;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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
	
	
}
