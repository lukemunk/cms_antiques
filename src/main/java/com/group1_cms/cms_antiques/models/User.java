package com.group1_cms.cms_antiques.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class User implements UserDetails {

    //private final String INVALID_PASSWORD_MESSAGE = "Password must be a minimum 8 characters (1 uppercase, 1 lowercase, 1 number)";

    private UUID id;

    //@NotEmpty(message = "*First Name required")
    private String firstName;

    //@NotEmpty(message = "*Last Name required")
    private String lastName;

    //@NotEmpty(message = "*Username required")
    private String username;

    @JsonIgnore
    //@NotEmpty(message = "*Password required")
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = INVALID_PASSWORD_MESSAGE)
    private String password;

    //@NotEmpty
    private String passwordConfirm;

    //@NotEmpty(message = "*Email required")
    //@Email(message = "Please enter a valid email address")
    private String email;

    private String phoneNum;

    private String imagePath;

    private Address address;

    @JsonIgnore
    private boolean locked = false;

    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    private ZonedDateTime credentialsExpiredOn;

    @JsonIgnore
    private ZonedDateTime expiredOn;

    private Map<UUID, Role> roles;

    private ZonedDateTime createdOn;

    private ZonedDateTime modifiedOn;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //@NotEmpty
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ZonedDateTime getCredentialsExpiredOn() {
        return credentialsExpiredOn;
    }

    public void setCredentialsExpiredOn(ZonedDateTime credentialsExpiredOn) {
        this.credentialsExpiredOn = credentialsExpiredOn;
    }

    public ZonedDateTime getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(ZonedDateTime expiredOn) {
        this.expiredOn = expiredOn;
    }

    public Map<UUID, Role> getRoles() {
        if(roles == null){
            roles = new HashMap<>();
        }
        return roles;
    }

    public Role getRoleById(UUID id){
        if(roles != null){
            return roles.get(id);
        }
        return null;
    }

    public void setRoles(Map<UUID, Role> roles) {
        this.roles = roles;
    }

    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(ZonedDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public ZonedDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(ZonedDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName())) //look at simplegrantedauthority
                .collect(Collectors.toSet());
    }

    public Set<Permission> getPermissions(){
        return Optional.ofNullable(this.roles)
                .orElseGet(HashMap::new)
                .values()
                .stream()
                .filter(role -> role.getPermissions() != null && role.getPermissions().size() > 0)
                .flatMap(role -> role.getPermissions().values().stream())
                .filter(p -> p != null && p.getName() != null && p.getId() != null)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return expiredOn == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if(credentialsExpiredOn != null){
            return ZonedDateTime.now().isAfter(credentialsExpiredOn);
        }
        else{
            return true;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
