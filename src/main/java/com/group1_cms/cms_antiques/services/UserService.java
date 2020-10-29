package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Role;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.repositories.RoleRepository;
import com.group1_cms.cms_antiques.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User.UserBuilder;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User userToSave, boolean encodePassword){

        User userFromDb = userRepository.getUserByUserName(userToSave.getUsername());

        if(userFromDb.getId() != null){ //check to see if user is already in database
            userToSave.setId(userFromDb.getId()); //set the users id to that of user in database to prevent multiples
            if(passwordEncoder.matches(userToSave.getPassword(), userFromDb.getPassword())){ //check if the password is the same
                userToSave.setPassword(userFromDb.getPassword()); //if it is, the set it to the same encrypted password from database
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
        Role role = roleRepository.getRoleByName("ROLE_Member");
        userRole.put(role.getId(), role);
        user.setRoles(userRole);
        user.setEnabled(true);
        user.setCreatedOn(ZonedDateTime.now());
        saveUser(user, true);
        User newUser = addUserRole(user, role);
        return newUser;
    }

    public User saveDefaultAdminUser(User user){
        Map<UUID, Role> adminRole = new LinkedHashMap<>();
        Role role = roleRepository.getRoleByName("ROLE_Admin");
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
            return userRepository.addUserRoles(user, role);
        }
        if(user != null){
            userToAddRole = userRepository.getUserByUserName(user.getUsername());
            if(userToAddRole != null){
                return userRepository.addUserRoles(userToAddRole, role);
            }
        }

        return null;
    }

    public boolean checkForDuplicateUser(String username){

        User user = userRepository.getUserByUserName(username);

        if(user.getId() == null){
            return false;
        }
        return true;
    }

    public User getUserFromUserDetails(UserDetails userDetails){
        User user = userRepository.getUserByUserName(userDetails.getUsername());
        if(user.getId() != null){
            return user;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.getUserByUserName(username);
        if(user.getUsername() == null){
            throw new UsernameNotFoundException("User not found using username " + username);
        }
        return user;
    }

}
