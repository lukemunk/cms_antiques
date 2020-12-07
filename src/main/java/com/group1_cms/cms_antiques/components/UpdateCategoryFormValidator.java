package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.Category;
import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.UserProfileDto;
import com.group1_cms.cms_antiques.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateCategoryFormValidator implements Validator {

    private final CategoryService categoryService;

    @Autowired
    public UpdateCategoryFormValidator(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
       return CategoryDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDto categoryDto = (CategoryDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryName", "Required.CategoryName");

        if(categoryDto.getPreviousName() == null || !categoryDto.getCategoryName().equalsIgnoreCase(categoryDto.getPreviousName())){
            if(categoryService.checkForDuplicateName(categoryDto.getCategoryName())){
                errors.rejectValue("categoryName", "Duplicate.CategoryName");
            }
        }

    }
}
