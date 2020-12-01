package com.group1_cms.cms_antiques.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserDataDto {

    private String id;
    private String fName;
    private String lName;
    private String fullName;
    private String fullName_lastFirst;
    private String username;
    private String previousUsername;
    private String password;
    private String passwordConfirm;
    private String email;
    private String previousEmail;
    private String phoneNumber;
    private String createdOn;
    private String locked;
    private boolean lockedUnlocked;
    private List<String> userRoles;

    public UserDataDto(){}

    public UserDataDto(UUID id, String fName, String lName, String email, String phoneNumber, ZonedDateTime createdON, boolean locked){
        this.id = id.toString();
        if(fName == null || fName.equals("")){
            this.fName = "Not Provided";
        }
        else{
            this.fName = fName;
        }
        if(lName == null || lName.equals("")){
            this.lName = "Not Provided";
        }
        else{
            this.lName = lName;
        }
        if(this.fName.equals("Not Provided") || this.lName.equals("Not Provided")){
            this.fullName = "Not Provided";
            this.fullName_lastFirst = "Not Provided";
        }
        else{
            this.fullName = fName + " " + lName;
            this.fullName_lastFirst = lName + ", " + fName;
        }
        this.email = email;
        if(phoneNumber == null || phoneNumber.equals("")){
            this.phoneNumber = "Not Provided";
        }
        else{
            this.phoneNumber = phoneNumber;
        }
        this.createdOn = createdON.format(DateTimeFormatter.ofPattern("d MMM uuuu"));
        if(locked){
            this.locked = "Locked";
        }
        if(!locked){
            this.locked = "Unlocked";
        }
        this.lockedUnlocked = locked;
    }

    public UserDataDto(UUID id, String fName, String lName, String username, String email,
                       ZonedDateTime createdOn, boolean locked, Map<UUID, Role> roleMap) {

        this(id, fName, lName, email, null, createdOn, locked);

        this.username = username;
        this.previousUsername = username;
        this.previousEmail = email;

        userRoles = getRoleListFromRoleMap(roleMap);
    }
        public String getId(){ return id; }

    public void setId(String id){
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getFullName_lastFirst() {
        return fullName_lastFirst;
    }

    public void setFullName_lastFirst(String fullName_lastFirst) {
        this.fullName_lastFirst = fullName_lastFirst;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPreviousUsername() {
        return previousUsername;
    }

    public void setPreviousUsername(String previousUsername) {
        this.previousUsername = previousUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreviousEmail() {
        return previousEmail;
    }

    public void setPreviousEmail(String previousEmail) {
        this.previousEmail = previousEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String lockedUnlocked) {
        this.locked = lockedUnlocked;
    }

    public boolean isLockedUnlocked() {
        return lockedUnlocked;
    }

    public void setLockedUnlocked(boolean locked) {
        lockedUnlocked = locked;
    }

    public List<String> getUserRoles() {
        if(userRoles == null){
            userRoles = new ArrayList<>();
        }
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    private List<String> getRoleListFromRoleMap(Map<UUID, Role> roleMap){
        List<String> userRoleList = new ArrayList<>();
        for(Role role: roleMap.values()) {
            userRoleList.add(role.getId().toString());
        }
        return userRoleList;
    }
}
