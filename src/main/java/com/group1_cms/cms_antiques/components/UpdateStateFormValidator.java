package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.StateDto;
import com.group1_cms.cms_antiques.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UpdateStateFormValidator implements Validator {

    private final StateService stateService;

    @Autowired
    public UpdateStateFormValidator(StateService stateService){
        this.stateService = stateService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return StateDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StateDto stateDto = (StateDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "stateName", "Required.StateName");

        if(stateDto.getPreviousName() == null || !stateDto.getStateName().equalsIgnoreCase(stateDto.getPreviousName())){
            if(stateService.checkForDuplicateName(stateDto.getStateName())){
                errors.rejectValue("stateName", "Duplicate.StateName");
            }
        }
    }
}
