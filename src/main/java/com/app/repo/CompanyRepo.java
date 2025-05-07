package com.app.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Company;

@Repository
public interface CompanyRepo extends MongoRepository<Company, String>{

	Optional<Company> findByName(String name);
}
