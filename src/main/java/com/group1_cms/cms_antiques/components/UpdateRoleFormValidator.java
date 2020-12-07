package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.RoleDto;
import com.group1_cms.cms_antiques.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateRoleFormValidator implements Validator {

    private final RoleService roleService;

    @Autowired
    public UpdateRoleFormValidator(RoleService roleService){
        this.roleService = roleService;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return RoleDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleDto roleDto = (RoleDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleName", "Required.RoleName");

        if(roleDto.getPreviousName() == null || !roleDto.getRoleName().equalsIgnoreCase(roleDto.getPreviousName())){
            if(roleService.checkForDuplicateName(roleDto.getRoleName())){
                errors.rejectValue("roleName", "Duplicate.RoleName");
            }
        }
    }
}
