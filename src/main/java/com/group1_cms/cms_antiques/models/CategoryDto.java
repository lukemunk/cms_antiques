package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CategoryDto {

    private String id;
    private String categoryName;
    private String previousName;
    private String createdOn;
    private String modifiedOn;

    public CategoryDto(){}

    public CategoryDto(UUID id, String categoryName, ZonedDateTime createdOn, ZonedDateTime modifiedOn){
        this.id = id.toString();
        this.categoryName = categoryName;
        this.previousName = categoryName;
        this.createdOn = createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.modifiedOn = modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPreviousName() {
        return previousName;
    }

    public void setPreviousName(String previousName) {
        this.previousName = previousName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
