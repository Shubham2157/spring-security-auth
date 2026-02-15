package com.shubham.authentication.controller;


import com.shubham.authentication.dto.UserRequest;
import com.shubham.authentication.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @PostMapping("/hi")
    public String hello(){
        return "hello user";
    }


    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody UserRequest userRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()));

        if (authenticate.isAuthenticated()){
            return jwtService.generateToken(userRequest.getUsername());
        }else {
            return  null;
        }
    }
}
