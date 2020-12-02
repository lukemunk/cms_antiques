package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.RoleDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateRoleFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return RoleDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleDto roleDto = (RoleDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName", "Required.RoleName");
    }
}
