package com.group1_cms.cms_antiques.models;

public class UserPasswordDto {

    private String userName;
    private String currentPassword;
    private String encryptedCurrentPassword;
    private String password;
    private String passwordConfirm;

    public UserPasswordDto() {
    }

    public UserPasswordDto( String userName, String currentPassword, String encryptedCurrentPassword, String password, String passwordConfirm){
        this.userName = userName;
        this.currentPassword = currentPassword;
        this.encryptedCurrentPassword = encryptedCurrentPassword;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getEncryptedCurrentPassword() {
        return encryptedCurrentPassword;
    }

    public void setEncryptedCurrentPassword(String encryptedCurrentPassword) {
        this.encryptedCurrentPassword = encryptedCurrentPassword;
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
}
