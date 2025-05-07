package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.exception.OrderException;
import com.app.exception.TimeStudyException;
import com.app.model.TimeStudy;
import com.app.response.ApiResponse;
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
	
	@GetMapping("/{styleNo}")
	public ResponseEntity<List<TimeStudy>>getStudyByStyleNo(@PathVariable String styleNo){
		return new ResponseEntity<List<TimeStudy>>(timeStudyService.getStudyByStyleNo(styleNo),HttpStatus.OK);
	}
	
	@GetMapping("/operator")
	public ResponseEntity<List<TimeStudy>> getStudyByOperatorId(@RequestParam String operatorId,@RequestParam String styleNo) throws TimeStudyException{
		return new ResponseEntity<List<TimeStudy>>(timeStudyService.getStudyByOperatorId(operatorId,styleNo),HttpStatus.OK);
	}
	
//	@PutMapping("/update")
//	public ResponseEntity<TimeStudy> updateLaps(@RequestBody LapsRequest laps) throws TimeStudyException, OrderException{
//		return new ResponseEntity<TimeStudy>(timeStudyService.updateLap(laps),HttpStatus.ACCEPTED);
//	}
//	
	@PutMapping("/remarks/{id}")
	public ResponseEntity<ApiResponse> updateRemarks(@PathVariable String id, @RequestParam String remarks) throws TimeStudyException{
		return new ResponseEntity<ApiResponse>(new ApiResponse(timeStudyService.updateRemarks(id,remarks)),HttpStatus.ACCEPTED); 
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<TimeStudy> updateLapsReading(@RequestBody TimeStudy study) throws TimeStudyException, OrderException{
		return new ResponseEntity<TimeStudy>(timeStudyService.updateLaps(study),HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> deleteOperatorById(@PathVariable String id){
		timeStudyService.deleteById(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Operator Deleted Successfully"),HttpStatus.OK);
	}
	
}
