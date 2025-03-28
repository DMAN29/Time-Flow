package com.app.model;

import java.time.LocalDateTime;
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
public class Order {

	@Id
	private String id;
	private String styleNo;
	private String itemNo;
	private String description;
	private String fabric;
	private String division;
	private String buyer;
	private Long orderQuantity;
	private Long target; // 1000
	private Integer lineDesign;
	private Double efficiency; // 80
	private Long designOutput;// 800
	private Double totalSam;
	private Double totalAllocation;
	private Double totalRequired;
	private List<Operation> operations;
	private LocalDateTime createdAt;
	private LocalDateTime deadLine;
	private Integer allowance = 0;
	private Integer lane=0;
	
}
