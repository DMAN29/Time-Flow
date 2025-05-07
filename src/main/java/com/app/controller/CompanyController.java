package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.CompanyException;
import com.app.exception.UserException;
import com.app.model.Company;
import com.app.response.ApiResponse;
import com.app.service.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	
	@PostMapping("")
	public ResponseEntity<Company> addNewCompay(@RequestHeader("Authorization")String token, @RequestBody Company company) throws UserException, CompanyException{
		return new ResponseEntity<Company>(companyService.addNewCompany(token,company),HttpStatus.CREATED);
	}
	
	
	@GetMapping("")
	public ResponseEntity<List<Company>> getCompanyList(){
		return new ResponseEntity<List<Company>>(companyService.getCompanyList(),HttpStatus.OK);
	}
	
//	@PutMapping("/update")
//	public ResponseEntity<ApiResponse> updateCompany(@RequestBody Company company, @RequestHeader("Authorization")String token){
//		companyService.updateCompany(company,token);
//		return new ResponseEntity<ApiResponse>(new ApiResponse("Company update to"+company.getName()),HttpStatus.ACCEPTED);
//	}
	
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<ApiResponse> deleteCompany(@PathVariable String name, @RequestHeader("Authorization")String token) throws UserException, CompanyException{
		companyService.deleteCompany(name,token);
		return new ResponseEntity<ApiResponse>(new ApiResponse(name+" removed from the company List"),HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/check/{name}")
	public ResponseEntity<Boolean> isCompanyInUse(@PathVariable String name) {
	    return new ResponseEntity<Boolean>(companyService.companyHasUsers(name),HttpStatus.OK);
	}

}
