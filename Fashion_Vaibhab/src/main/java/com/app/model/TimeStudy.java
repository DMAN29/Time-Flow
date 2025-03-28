package com.app.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeStudy {

	@Id
	private String id;
	private String styleNo;
	private String operatorName;
	private String operatorId;
	private String operationName;
	private String section;
	private String machineType;
	private List<String> laps; // mm:ss:SSS format
	private List<Long> lapsMS; // Received in request (milliseconds)
	private String avgTime; // Average time in mm:ss:SSS
	private Integer capacityPH; // Production Per Hour
	private Integer capacityPD;
	private String allowanceTime;
	private String remarks;
	private Double assigned; // 0.5 or 1
}


// lap count and allowance change