package com.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.TimeStudy;

public interface TimeStudyRepo extends MongoRepository<TimeStudy, String>{

//	Optional<TimeStudy> findByOperatorId(String operatorId);
	
	Optional<TimeStudy> findById(String id);
    Optional<List<TimeStudy>> findByStyleNoAndOperatorId(String styleNo, String operatorId);
    
    boolean existsByStyleNoAndOperatorId(String styleNo, String operatorId);

	Optional<List<TimeStudy>> findByStyleNo(String styleNo);
}
