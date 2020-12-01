package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.Category;
import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.UserProfileDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateCategoryFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
       return CategoryDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDto categoryDto = (CategoryDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryName", "Required.CategoryName");
    }
}
