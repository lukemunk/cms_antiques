package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.UserDataDto;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateUserFormValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UpdateUserFormValidator(UserService userService){
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDataDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDataDto userDataDto = (UserDataDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fName", "Required.FirstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lName", "RequiredLastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required.Username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Required.Email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required.Password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Required.PasswordConfirm");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userRoles", "Required.UserRoles");

        if (userDataDto.getPassword().length() < 8 && userDataDto.getPassword().length() > 0){
            errors.rejectValue("password", "Invalid.Password");
        }
        if(!userDataDto.getPassword().equals(userDataDto.getPasswordConfirm()) && userDataDto.getPasswordConfirm().length() > 0){
            errors.rejectValue("passwordConfirm", "Diff.PasswordConfirm");
        }

        if(userDataDto.getPreviousUsername() == null || !userDataDto.getUsername().equalsIgnoreCase(userDataDto.getPreviousUsername())){
            if(userService.checkForDuplicateUser(userDataDto.getUsername())){
                errors.rejectValue("username", "Duplicate.Username");
            }
        }
        if(userDataDto.getPreviousEmail() == null || !userDataDto.getEmail().equalsIgnoreCase(userDataDto.getPreviousEmail())){
            if(userService.checkForDuplicateUser(userDataDto.getEmail())){
                errors.rejectValue("email", "Duplicate.Email");
            }
        }
    }
}
