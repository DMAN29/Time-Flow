package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.UserException;
import com.app.model.Role;
import com.app.model.User;
import com.app.response.ApiResponse;
import com.app.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt) throws UserException {
		return new ResponseEntity<>(userService.getAllUsers(jwt), HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {
		User user = userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PutMapping("/change-role")
	public ResponseEntity<ApiResponse> changeUserRole(@RequestHeader("Authorization") String jwt,
			@RequestParam String email, @RequestParam Role role) throws UserException {
		userService.changeRole(jwt, email, role);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Role Changed For Email : " + email),
				HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse> deleteUser(@RequestHeader("Authorization") String jwt,
			@RequestParam String email) throws UserException {
		userService.deleteUser(jwt, email);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted with email: " + email),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/pause-role")
	public ResponseEntity<ApiResponse> pauseUserRole(@RequestHeader("Authorization") String jwt,
			@RequestParam String email) throws UserException {
		userService.pauseRole(jwt, email);
		return new ResponseEntity<>(new ApiResponse("All roles paused for email: " + email), HttpStatus.ACCEPTED);
	}

}
