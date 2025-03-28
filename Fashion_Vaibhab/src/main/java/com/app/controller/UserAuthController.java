	package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.UserException;
import com.app.model.User;
import com.app.response.AuthResponse;
import com.app.service.UserService;

@RestController
public class UserAuthController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) throws UserException {
		userService.createUser(user);
		return new ResponseEntity<>("User Created Successfully",HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody User user) throws UserException {
		String token = userService.verifyUser(user);
		AuthResponse authResponse = new AuthResponse(token,"Signin Success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.ACCEPTED);
	}
}
