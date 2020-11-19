package com.group1_cms.cms_antiques.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ClassifiedAd {
	private UUID id;
	private String title;
	private String description;
	private String price;
	private Item item;
	private User creator;
	private LocalDateTime endDate;
	private List<String> tags;
	
	public ClassifiedAd() {
		
	}
	
	public ClassifiedAd(String title, String description, String price, Item item, User creator, LocalDateTime endDate) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.item = item;
		this.creator = creator;
		this.endDate = endDate;
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
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
}
