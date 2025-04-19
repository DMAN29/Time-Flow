package com.app.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.Company;

public interface CompanyRepo extends MongoRepository<Company, String>{

	Optional<Company> findByName(String name);
}
