package com.group1_cms.cms_antiques.models;

public class Item {
	private String name;
	private String category;
	
	public Item() {
		name = "Antique";
		category = "Furniture";
	}
	
	public Item(String name, String category) {
		this.name = name;
		this.category = category;
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
	
	
}
