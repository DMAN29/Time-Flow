package com.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
	private int id;
	private String operationName;
	private String section;
	private Double sam;
	private String machineType;
	private Double required;
	private Double allocated;
	private Long target;
//	private Long skillMatrix;
	
	
}
