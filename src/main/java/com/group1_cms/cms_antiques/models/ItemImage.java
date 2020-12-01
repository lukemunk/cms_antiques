package com.group1_cms.cms_antiques.models;

import java.util.UUID;

public class ItemImage {
	private String fileName;
	private UUID id;
	

	
	public ItemImage() {
		super();
	}
	
	public String getFilePath() {
		return "~/item_images/"+id+"/"+fileName;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	
	
	

}
