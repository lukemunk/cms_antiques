package com.group1_cms.cms_antiques.models;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ItemImage {
	private String fileName;
	private UUID id;
	private BufferedImage image;
;
	
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
