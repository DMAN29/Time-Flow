package com.app.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.TimeStudy;

public interface TimeStudyRepo extends MongoRepository<TimeStudy, String>{

//	Optional<TimeStudy> findByOperatorId(String operatorId);
	

    Optional<TimeStudy> findByStyleNoAndOperatorId(String styleNo, String operatorId);
    
    boolean existsByStyleNoAndOperatorId(String styleNo, String operatorId);
}
