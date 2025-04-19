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

import com.app.exception.CompanyException;
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
//	    roles.add(Role.ROLE_USER);
	    user.setRole(roles);	
	    user.setCreatedAt(LocalDateTime.now());
	    userRepo.save(user);
	}



	public List<User> getAllUsers(String token) throws UserException {
	    User user = findUserProfileByJwt(token);

	    if (user.getRole().contains(Role.ROLE_HEAD)) {
	        return userRepo.findAll();
	    } else if (user.getRole().contains(Role.ROLE_ADMIN)) {
	        if (user.getCompany() == null || user.getCompany().trim().isEmpty()) {
	            throw new UserException("You don't have a company, so you can't access this resource");
	        }
	        return userRepo.findByCompany(user.getCompany());
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


	public void changeRole(String token, String email, Role newRole) throws UserException {
	    User currentUser = findUserProfileByJwt(token);
	    User targetUser = getUserByEmail(email);

	    if (targetUser.getRole().contains(Role.ROLE_HEAD)) {
	        throw new UserException("Cannot change role of HEAD user");
	    }

	    boolean isAdmin = currentUser.getRole().contains(Role.ROLE_ADMIN);
	    boolean isHead = currentUser.getRole().contains(Role.ROLE_HEAD);

	    // ❌ Neither Admin nor Head
	    if (!isAdmin && !isHead) {
	        throw new UserException("Only ADMIN or HEAD can change roles");
	    }

	    // ❌ Head cannot change own role
	    if (isHead && currentUser.getEmail().equalsIgnoreCase(email)) {
	        throw new UserException("HEAD cannot change their own role");
	    }

	    // ✅ HEAD can change anyone (except HEADs)
	    if (isHead) {
	        targetUser.getRole().clear();
	        targetUser.getRole().add(newRole);
	        if (newRole == Role.ROLE_ADMIN) {
	            targetUser.getRole().add(Role.ROLE_USER); // Admin inherits USER
	        }
	        userRepo.save(targetUser);
	        return;
	    }

	    // ✅ ADMIN logic
	    if (isAdmin) {
	        boolean isTargetAdmin = targetUser.getRole().contains(Role.ROLE_ADMIN);
	        boolean isTargetHead = targetUser.getRole().contains(Role.ROLE_HEAD);

	        // ❌ Admin cannot change HEAD or another ADMIN
	        if (isTargetAdmin || isTargetHead) {
	            throw new UserException("Admin cannot change role of Admin or Head");
	        }

	        // ❌ Admin cannot assign HEAD
	        if (newRole == Role.ROLE_HEAD) {
	            throw new UserException("Admin cannot assign HEAD role");
	        }

	        // ✅ Can change users with no role or ROLE_USER
	        targetUser.getRole().clear();
	        targetUser.getRole().add(newRole);
	        if (newRole == Role.ROLE_ADMIN) {
	            targetUser.getRole().add(Role.ROLE_USER);
	        }

	        userRepo.save(targetUser);
	    }
	}




	public void deleteUser(String token, String email) throws UserException {
	    // 🔐 Get the user who is making the request
	    User admin = findUserProfileByJwt(token);

	    // 👤 Get the user who is supposed to be deleted
	    User user = getUserByEmail(email);

	    // ⛔ Prevent a user from deleting themselves
	    if (admin.getId().equals(user.getId())) {
	        throw new UserException("You cannot delete yourself!");
	    }

	    // 👑 If HEAD, can delete anyone
	    if (admin.getRole().contains(Role.ROLE_HEAD)) {
	        userRepo.deleteById(user.getId());
	        return;
	    }

	    // 🛡 If ADMIN
	    if (admin.getRole().contains(Role.ROLE_ADMIN)) {
	        // ❌ Can't delete another ADMIN or HEAD
	        if (user.getRole().contains(Role.ROLE_ADMIN) || user.getRole().contains(Role.ROLE_HEAD)) {
	            throw new UserException("Admins cannot delete other admins or heads!");
	        }

	        // ✅ Can delete normal users
	        userRepo.deleteById(user.getId());
	        return;
	    }

	    // ❌ No permission at all (e.g. ROLE_USER)
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

	public boolean companyHasUsers(String companyName) {
	    return userRepo.existsByCompany(companyName);
	}
	
	
	

}
