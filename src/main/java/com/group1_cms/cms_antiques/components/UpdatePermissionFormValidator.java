package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.PermissionDto;
import com.group1_cms.cms_antiques.models.StateDto;
import com.group1_cms.cms_antiques.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdatePermissionFormValidator implements Validator {

    private final PermissionService permissionService;

    @Autowired
    public UpdatePermissionFormValidator(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PermissionDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PermissionDto permissionDto = (PermissionDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "permissionName", "Required.PermissionName");

        if(permissionDto.getPreviousName() == null || !permissionDto.getPermissionName().equalsIgnoreCase(permissionDto.getPreviousName())){
            if(permissionService.checkForDuplicateName(permissionDto.getPermissionName())){
                errors.rejectValue("permissionName", "Duplicate.PermissionName");
            }
        }
    }
}
