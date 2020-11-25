package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RegistrationFormValidator implements Validator {

    private UserService userService;

    @Autowired
    public RegistrationFormValidator(UserService userService){
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "Required.FirstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "RequiredLastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required.Username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Required.Email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required.Password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Required.PasswordConfirm");

        if (user.getPassword().length() < 8 && user.getPassword().length() > 0){
            errors.rejectValue("password", "Invalid.Password");
        }
        if(!user.getPassword().equals(user.getPasswordConfirm()) && user.getPasswordConfirm().length() > 0){
            errors.rejectValue("passwordConfirm", "Diff.PasswordConfirm");
        }
        if(userService.checkForDuplicateUser(user.getUsername())){
            errors.rejectValue("username", "Duplicate.Username");
        }
        if(userService.checkForDuplicateUser(user.getEmail())){
            errors.rejectValue("email", "Duplicate.Email");
        }
    }
}
