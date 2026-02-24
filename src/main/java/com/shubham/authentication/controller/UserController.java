package com.shubham.authentication.controller;


import com.shubham.authentication.constant.Role;
import com.shubham.authentication.dto.UserRequest;
import com.shubham.authentication.entity.Users;
import com.shubham.authentication.repo.UserRepo;
import com.shubham.authentication.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/hi")
    public String hello(){
        return "hello user";
    }


    @PostMapping
    public Users createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Role role
    ){
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(passwordEncoder.encode(password));
        users.setRole(role);
        users.setActive(true);
        userRepo.save(users);
        return users;
    }


    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody UserRequest userRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()));

        if (authenticate.isAuthenticated()){
            String role = authenticate
                    .getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority()
                    .replace("ROLE_", "");
            return jwtService.generateToken(userRequest.getUsername(), role);
        }else {
            return  null;
        }
    }
}
