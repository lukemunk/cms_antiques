package com.group1_cms.cms_antiques.components;

import com.group1_cms.cms_antiques.models.UserProfileDto;
import com.group1_cms.cms_antiques.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UserProfileFormValidator implements Validator{

    private final UserService userService;

    @Autowired
    public UserProfileFormValidator(UserService userService){
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserProfileDto.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserProfileDto userProfileDto = (UserProfileDto) target;
        boolean isValidAddress = false;
        String phoneNumRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

        String zipCodeRegex = "^\\d{5}(-\\d{4})?$";

        Pattern phoneNumPattern = Pattern.compile(phoneNumRegex);
        Pattern zipCodePattern = Pattern.compile(zipCodeRegex);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "Required.FirstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Required.LastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "Required.Username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Required.Email");

        isValidAddress = getValidAddressCityState(userProfileDto.getAddressLine1(), userProfileDto.getCityName(),
                userProfileDto.getZipcode(), userProfileDto.getStateName());
        if(userProfileDto.isAddressProvided() && !isValidAddress){
            errors.reject("Invalid.Address");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "Required.AddressLine1");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cityName", "Required.City");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "stateName", "Required.State");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zipcode", "Required.Zipcode");

        }

        if(userProfileDto.isAddressProvided() && userProfileDto.getZipcode() != ""){
            if(!zipCodePattern.matcher(userProfileDto.getZipcode()).matches()){
                errors.rejectValue("zipcode", "Invalid.Zipcode");
            }
        }

        if(!userProfileDto.getUserName().equalsIgnoreCase(userProfileDto.getOlduserName())){
            if(userService.checkForDuplicateUser(userProfileDto.getUserName())){
                errors.rejectValue("userName", "Duplicate.Username");
            }
        }
        if(!userProfileDto.getEmail().equalsIgnoreCase(userProfileDto.getOldEmail())){
            if(userService.checkForDuplicateUser(userProfileDto.getEmail())){
                errors.rejectValue("email", "Duplicate.Email");
            }
        }
        if(!(userProfileDto.getPhoneNum() == "" || userProfileDto.getPhoneNum() == null)){
            if(!phoneNumPattern.matcher(userProfileDto.getPhoneNum()).matches()){
                errors.rejectValue("phoneNum", "Invalid.PhoneNumber");
            }
        }



    }
    private boolean getValidAddressCityState(String streetAddress, String city, String zip, String state){
        String[] fieldList = {streetAddress, city, zip, state};
        int emptyFieldCount = 0;
        for(int i = 0; i < fieldList.length; i++){
            if(fieldList[i].equals("")){
                emptyFieldCount++;
            }
        }
        if(emptyFieldCount == 0){
            return true;
        }
        else{
            return false;
        }
    }
}
