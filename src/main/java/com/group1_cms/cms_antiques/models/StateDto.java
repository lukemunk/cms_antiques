package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class StateDto {

    private String id;
    private String stateName;
    private String createdOn;
    private String modifiedOn;

    public StateDto(){ }

    public StateDto(UUID id, String stateName, ZonedDateTime createdOn, ZonedDateTime modifiedOn){
        this.id = id.toString();
        this.stateName = stateName;
        this.createdOn = createdOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        this.modifiedOn = modifiedOn.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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
