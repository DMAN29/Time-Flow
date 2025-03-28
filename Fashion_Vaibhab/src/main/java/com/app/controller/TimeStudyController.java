package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.OrderException;
import com.app.exception.TimeStudyException;
import com.app.model.TimeStudy;
import com.app.request.LapsRequest;
import com.app.request.OperatorRequest;
import com.app.request.RemarkRequest;
import com.app.service.TimeStudyService;

@RestController
@RequestMapping("/api/study")
public class TimeStudyController {

	@Autowired
	private TimeStudyService timeStudyService;
	
	@PostMapping("")
	public ResponseEntity<TimeStudy> storeStudy(@RequestBody TimeStudy study) throws TimeStudyException, OrderException{
		return new ResponseEntity<TimeStudy>(timeStudyService.storeStudy(study),HttpStatus.CREATED);
	}
	
	@GetMapping("")
	public ResponseEntity<List<TimeStudy>> getAllStudy(){
		return new ResponseEntity<List<TimeStudy>>(timeStudyService.getAllStudy(),HttpStatus.OK);
	}
	
	@GetMapping("/operator")
	public ResponseEntity<TimeStudy> getStudyByOperatorId(@RequestBody OperatorRequest operator) throws TimeStudyException{
		return new ResponseEntity<TimeStudy>(timeStudyService.getStudyByOperatorId(operator),HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<TimeStudy> updateLaps(@RequestBody LapsRequest laps) throws TimeStudyException, OrderException{
		return new ResponseEntity<TimeStudy>(timeStudyService.updateLap(laps),HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/remarks")
	public ResponseEntity<String> updateRemarks(@RequestBody RemarkRequest remarks) throws TimeStudyException{
		return new ResponseEntity<String>(timeStudyService.updateRemarks(remarks),HttpStatus.ACCEPTED);
	}
	
}
