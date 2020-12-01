package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.StateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateStateFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return StateDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StateDto stateDto = (StateDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "stateName", "Required.StateName");
    }
}
