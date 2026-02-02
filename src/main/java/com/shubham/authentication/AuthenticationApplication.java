package com.shubham.authentication;

import com.shubham.authentication.entity.Users;
import com.shubham.authentication.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class AuthenticationApplication {

	@Autowired
    UserRepo userRepo;

	@Autowired
    PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApplication.class, args);
	}


	@PostConstruct
	public void createUser(){
		Users users = new Users();
		users.setUsername("Shubham");
		users.setPassword(passwordEncoder.encode("12345"));
		users.setActive(true);

		//userRepo.save(users);
		System.out.println("User Inserted");
	}

}
