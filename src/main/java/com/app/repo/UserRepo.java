package com.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.model.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>{

	Optional<User> findByEmail(String email);

	Optional<User> findAllById(String id);

	List<User> findByCompany(String company);

	boolean existsByCompany(String name);

	boolean existsByEmail(String email);

	boolean existsByUserId(String userId);

}
