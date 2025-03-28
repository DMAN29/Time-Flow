package com.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.exception.UserException;
import com.app.model.Role;
import com.app.model.User;
import com.app.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	private BCryptPasswordEncoder  encoder = new BCryptPasswordEncoder(12);
	
	public String verifyUser(User user) throws UserException{
		Authentication authentication  = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
		
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(user.getEmail());
		}
		throw new UserException("User with UserName and Password not found");
	}

	public void createUser(User user) throws UserException {
		Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
	    if (existingUser.isPresent()) {
	        throw new UserException("User with this email already exists");
	    }
	    user.setPassword(encoder.encode(user.getPassword()));
	    List<Role> roles = new ArrayList<>();
	    roles.add(Role.ROLE_USER);
	    user.setRole(roles);
	    user.setCreatedAt(LocalDateTime.now());
	    userRepo.save(user);
	}

	
	public List<User> getAllUsers(String token) throws UserException {
	    User user = findUserProfileByJwt(token);
	    
	    if (user.getRole().contains(Role.ROLE_ADMIN) || user.getRole().contains(Role.ROLE_HEAD)) {            
	        return userRepo.findAll();
	    }
	    
	    throw new UserException("Only Admins and Heads can access this data");
	}

	public User getUserById(String id) throws UserException {
		Optional<User> user = userRepo.findAllById(id);
		if(user.isEmpty()) {
			throw new UserException("User with Id: "+id+" not Present");
		}
		return user.get();
	}
	public User getUserByEmail(String email) throws UserException {
		Optional<User> user = userRepo.findByEmail(email);
		if(user.isEmpty()) {
			throw new UserException("User With email: "+email+" Not Exist");
		}
		return user.get();
	}


	public void changeRole(String token, String email) throws UserException {
	    User admin = findUserProfileByJwt(token);
	    
	    if (!admin.getRole().contains(Role.ROLE_ADMIN)) {
	        throw new UserException("Only Admin can Change Role");
	    }

	    User user = getUserByEmail(email);

	    if (user.getRole().contains(Role.ROLE_HEAD)) {
	        throw new UserException("Cannot change role of HEAD user");
	    }

	    if (!user.getRole().contains(Role.ROLE_ADMIN)) {
	        user.getRole().add(Role.ROLE_ADMIN);
	        userRepo.save(user);
	    }
	}


	public void deleteUser(String token, String email) throws UserException {
	    User admin = findUserProfileByJwt(token);  // User making the request
	    User user = getUserByEmail(email);  // User to be deleted

	    if (admin.getEmail().equals(user.getEmail())) {
	        throw new UserException("You cannot delete yourself!");
	    }

	    if (admin.getRole().contains(Role.ROLE_HEAD)) {
	        userRepo.deleteById(user.getId());
	        return;
	    }

	    if (admin.getRole().contains(Role.ROLE_ADMIN)) {
	        if (user.getRole().contains(Role.ROLE_ADMIN) || user.getRole().contains(Role.ROLE_HEAD)) {
	            throw new UserException("Admins cannot delete other admins or heads!");
	        } else {
	            userRepo.deleteById(user.getId()); // Admin can delete users only
	            return;
	        }
	    }

	    throw new UserException("You do not have permission to delete users!");
	}

	public User findUserProfileByJwt(String jwt) throws UserException {
		String email = jwtService.getEmailFromToken(jwt);
		Optional<User> user = userRepo.findByEmail(email);
		
		if(user.isEmpty()) {
			throw new UserException("User Not found with Email : "+email );
		}
		return user.get();
	}
	
	

}
