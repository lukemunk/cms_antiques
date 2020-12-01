package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.StateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdatePermissionFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PermissionDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PermissionDto permissionDto = (PermissionDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permissionName", "Required.PermissionName");
    }
}
