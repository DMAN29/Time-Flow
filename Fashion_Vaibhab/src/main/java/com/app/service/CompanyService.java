package com.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.exception.CompanyException;
import com.app.exception.UserException;
import com.app.model.Company;
import com.app.model.Role;
import com.app.model.User;
import com.app.repo.CompanyRepo;

public class CompanyService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyRepo companyRepo;
	
	public Company addNewCompany(String token, Company company) throws UserException, CompanyException {
		User user = userService.findUserProfileByJwt(token);
		if(user.getRole().contains(Role.ROLE_HEAD)) {
			Optional<Company> existing = companyRepo.findByName(company.getName());
			if(existing.isPresent()) {
				throw new CompanyException("Company already exitst with name :"+company.getName());
			}
			company.setCreatedAt(LocalDateTime.now());
			company.setUpdatedAt(LocalDateTime.now());
			companyRepo.save(company);
		}
		throw new UserException("You are not authorized to add companyies");
	}

	public List<Company> getCompanyList() {
		return companyRepo.findAll();
	}

//	public void updateCompany(Company company, String token) {
//		User user = userService.findUserProfileByJwt(token);
//		if(user.getRole().contains(Role.ROLE_HEAD)) {
//			Optional<Company> existing = companyRepo.findById(company.getId());
//			if()
//		}
//	}

	public void deleteCompany(String id, String token) throws UserException, CompanyException {
		User user = userService.findUserProfileByJwt(token);
		if(user.getRole().contains(Role.ROLE_HEAD)) {
			Optional<Company> existing = companyRepo.findById(id);
			if(existing.isPresent()) {
				companyRepo.deleteById(id);
			}
			throw new CompanyException("company with id :"+id +" not exist");
		}		
		throw new UserException("You are not authorized to delete company");
		
	}

}
