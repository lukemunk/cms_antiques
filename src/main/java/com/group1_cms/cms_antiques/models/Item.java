package com.group1_cms.cms_antiques.models;

import java.util.UUID;

public class Item {
	private UUID id;
	private String name;
	private String category;
	private ItemImage itemImage;
	
	public Item() {}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ItemImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(ItemImage itemImage) {
		this.itemImage = itemImage;
	}

	
	
	
	
	
}
