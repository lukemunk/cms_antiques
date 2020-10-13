package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user, boolean encodePassword){
        if (user.getId() == null){
            user.setId(UUID.randomUUID());
        }

        if(user.getPassword() != null && encodePassword){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return null; //implement save user in user repository
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found using username " + username);
        }
        return user;
    }
}
