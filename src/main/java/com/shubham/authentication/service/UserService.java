package com.shubham.authentication.service;

import com.shubham.authentication.entity.Users;
import com.shubham.authentication.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;


    public Users getUserDetails(String userName) {
        return userRepo.findByUsernameAndIsActive(userName, true)
                .orElseThrow(() ->
                        new UsernameNotFoundException("username not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = getUserDetails(username);

        return User.builder()
                .username(users.getUsername())
                .password(users.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
