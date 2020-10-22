package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RegistrationFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    private UserService userService;

    @Autowired
    public RegistrationFormValidator(UserService userService){
        this.userService = userService;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "FirstNameRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "LastNameRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "UsernameRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "EmailRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "PasswordRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "PasswordConfirmRequired");

        if (user.getPassword().length() < 8 && user.getPassword().length() > 0){
            errors.rejectValue("password", "Invalid.Password");
        }
        if(!user.getPassword().equals(user.getPasswordConfirm()) && user.getPasswordConfirm().length() > 0){
            errors.rejectValue("passwordConfirm", "Diff.PasswordConfirm");
        }
        if(userService.loadUserByUsername(user.getUsername()) != null){
            errors.rejectValue("username", "Duplicate.Username");
        }
    }
}
