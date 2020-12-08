package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.*;
import com.group1_cms.cms_antiques.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.*;

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleSerivce;
    private final StateService stateService;
    private final CityService cityService;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, StateService stateService,
                       CityService cityService, AddressService addressService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleSerivce = roleService;
        this.stateService = stateService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User userToSave, boolean encodePassword){

        User userFromDb = userRepository.findUserByUserName(userToSave.getUsername());

        if(userFromDb.getId() != null){ //check to see if user is already in database
            userToSave.setId(userFromDb.getId()); //set the users id to that of user in database to prevent multiples
            
            if(passwordEncoder.matches(userToSave.getPassword(), userFromDb.getPassword())){ //check if the password is the same
                userToSave.setPassword(userFromDb.getPassword()); //if it is, then set it to the same encrypted password from database
            }
            else{
                userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); //If the user changed their password
            }                                                                             //encrypt it before saving to database
            //if user is in database get the random UUID so second user isn't added
            return userRepository.save(userToSave);
        }

        if (userToSave.getId() == null){ //if user is not in database, set random UUID
            userToSave.setId(UUID.randomUUID());
        }

        if(userToSave.getPassword() != null && encodePassword){
            userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword())); //encode password before saving
        }

        return userRepository.save(userToSave); //save user to database
    }

    public User saveNewUser(User user){
        Map<UUID, Role> userRole = new LinkedHashMap<>();
        Role role = roleSerivce.findRoleByName("ROLE_Member");
        userRole.put(role.getId(), role);
        user.setRoles(userRole);
        user.setEnabled(true);
        user.setCreatedOn(ZonedDateTime.now());
        saveUser(user, true);
        User newUser = addUserRole(user, role);
        return newUser;
    }

    public void saveUserWithRolesFromUserDataDto(UserDataDto user){
        User userFromDb;
        if(user.getId() == null){
            userFromDb = new User();
            userFromDb.setId(UUID.randomUUID());
            userFromDb.setCreatedOn(ZonedDateTime.now());
        }
        else{
            userFromDb = getUserById(user.getId());
            userRepository.deleteUser_Role(user.getId());
        }
        userFromDb.setFirstName(user.getfName());
        userFromDb.setLastName(user.getlName());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setUsername(user.getUsername());
        userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userFromDb.setLocked(user.isLockedUnlocked());
        userFromDb.setModifiedOn(ZonedDateTime.now());

        User savedUser = userRepository.save(userFromDb);
        for(String role_id: user.getUserRoles()){
            userRepository.addToUser_Role(userFromDb.getId().toString(), role_id);
        }

    }

    public User saveDefaultAdminUser(User user){
        Map<UUID, Role> adminRole = new LinkedHashMap<>();
        Role role = roleSerivce.findRoleByName("ROLE_Admin");
        adminRole.put(role.getId(), role);
        user.setRoles(adminRole);
        user.setEnabled(true);
        user.setCreatedOn(ZonedDateTime.now());  //edit setexpiredon and setcredentialsexpiredon
        saveUser(user, true);
        User adminUser = addUserRole(user, role);
        return adminUser;
    }

    public User addUserRole(User user, Role role){
        User userToAddRole;

        if(user != null && user.getId() != null){
            return userRepository.addRoles(user, role);
        }
        if(user != null){
            userToAddRole = userRepository.findUserByUserName(user.getUsername());
            if(userToAddRole != null){
                return userRepository.addRoles(userToAddRole, role);
            }
        }

        return null;
    }

    public boolean checkForDuplicateUser(String username){

        User user = userRepository.findUserByUserName(username);

        if(user.getId() == null){
            return false;
        }
        return true;
    }

    public void resetUserPassword(UserPasswordDto userPasswordDto){

        if(userPasswordDto != null && userPasswordDto.getPassword() != null){
            userRepository.updatePassword(userPasswordDto.getUserName(),
                    passwordEncoder.encode(userPasswordDto.getPassword()));
        }
        return;
    }

    public UserProfileDto getUserWithProfileInfo(String username){
        UserProfileDto userProfileDto = new UserProfileDto();
        User user = userRepository.findUserAndAddressAndCityAndStateByUserName(username);
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setUserName(user.getUsername());
        userProfileDto.setEmail(user.getEmail());
        if(user.getPhoneNum() != null){
            userProfileDto.setPhoneNum(user.getPhoneNum());
        }
        if(user.getImagePath() != null){
            userProfileDto.setImagePath(user.getImagePath());
        }
        if(user.getAddress() != null){
            userProfileDto.setAddressLine1(user.getAddress().getStreetAddress());
            if(user.getAddress().getStreetAddressLine2() != null){
                userProfileDto.setAddressLine2(user.getAddress().getStreetAddressLine2());
            }
            userProfileDto.setCityName(user.getAddress().getCity().getName());
            userProfileDto.setZipcode(user.getAddress().getCity().getPostalCode());
            userProfileDto.setStateName(user.getAddress().getCity().getState().getName());
            userProfileDto.setAddressProvided(true);
        }
        else{
            userProfileDto.setAddressProvided(false
            );
        }
        return userProfileDto;
    }

    public User saveUserProfile(UserProfileDto userProfileDto){
        User userToSave = userRepository.findUserAndAddressAndCityAndStateByUserName(userProfileDto.getOlduserName());
        State stateFromDb;
        City cityToSave = new City();
        Address addressToSave = new Address();
        userToSave.setModifiedOn(ZonedDateTime.now());
        userToSave.setUsername(userProfileDto.getUserName());
        userToSave.setFirstName(userProfileDto.getFirstName());
        userToSave.setLastName(userProfileDto.getLastName());
        userToSave.setEmail(userProfileDto.getEmail());
        userToSave.setPhoneNum(userProfileDto.getPhoneNum());
        if(userToSave.getAddress() != null && !userProfileDto.isAddressProvided()){
            userToSave.getAddress().setId(null);
            userToSave.setModifiedOn(ZonedDateTime.now());
           // userRepository.setUserAddressIdToNullOnAddressDelete(userToSave.getUsername());
        }
        else if(userProfileDto.getAddressLine1() != "" && userProfileDto.getAddressLine1() != null){
            stateFromDb = stateService.findStateByName(userProfileDto.getStateName());
            cityToSave.setName(userProfileDto.getCityName());
            cityToSave.setPostalCode(userProfileDto.getZipcode());
            cityToSave = cityService.saveCity(cityToSave, stateFromDb);
            addressToSave.setStreetAddress(userProfileDto.getAddressLine1());
            addressToSave.setStreetAddressLine2(userProfileDto.getAddressLine2());
            addressToSave = addressService.saveAddress(addressToSave, cityToSave);
            userToSave.setAddress(addressToSave);
        }
       return userRepository.save(userToSave);
    }

    public List<UserDataDto> getUsersData(){
        List<UserDataDto> userDataDtoList = new ArrayList<>();
        for(User user: userRepository.findAllUsers()){
            userDataDtoList.add(
                new UserDataDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNum(),
                        user.getCreatedOn(), user.isLocked())
            );
        }
        return userDataDtoList;
    }

    public User getUserFromUserDetails(UserDetails userDetails){
        User user = userRepository.findUserByUserName(userDetails.getUsername());
        if(user.getId() != null){
            return user;
        }
        return null;
    }

    public User getUserById(String id){

        User userFromDb = userRepository.findUserById(id);
        if(userFromDb.getId() != null){
            return userFromDb;
        }
        return null;
    }

    public UserDataDto getUserDataDtoByUser(String id){

        User userFromDb = userRepository.findUserById(id);
        if(userFromDb.getId() != null){
            return new UserDataDto(userFromDb.getId(), userFromDb.getFirstName(), userFromDb.getLastName(),
                    userFromDb.getUsername(), userFromDb.getEmail(), userFromDb.getCreatedOn(),
                    userFromDb.isLocked(), userFromDb.getRoles());
        }
        return null;
    }

    public boolean deleteUserFromDbById(String id){
        if(userRepository.deleteUserById(id) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserByUserName(username);
        if(user.getUsername() == null){
            throw new UsernameNotFoundException("User not found using username " + username);
        }
        return user;
    }

}
