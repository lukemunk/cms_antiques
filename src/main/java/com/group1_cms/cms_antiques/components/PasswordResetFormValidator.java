package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.UserPasswordDto;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PasswordResetFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserPasswordDto.class.equals(aClass);
    }

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetFormValidator(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPasswordDto userPasswordDto = (UserPasswordDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "CurrentPasswordRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NewPasswordRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "ConfirmNewPasswordRequired");

        if(userPasswordDto.getCurrentPassword().length() > 0 && !passwordEncoder.matches(userPasswordDto.getCurrentPassword(), userPasswordDto.getEncryptedCurrentPassword())){
            errors.rejectValue("currentPassword", "Diff.CurrentPassword");
        }
        if(!userPasswordDto.getPassword().equals(userPasswordDto.getPasswordConfirm()) && userPasswordDto.getPasswordConfirm().length() > 0){
            errors.rejectValue("passwordConfirm", "Diff.NewPasswordConfirm");
        }
    }
}
