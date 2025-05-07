package com.app.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SkillMatrix {

	@Id
	private String Id;
	private String operatorName; // userName;
	private String operatorId; // userId;
	private String DOJ;
	private List<Map<Map<String,Critical>,Long>> skill; //stores skills percentage 
	private Long total; // count the length of skills
}
