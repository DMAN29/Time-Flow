package com.app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LapsCountRequest {

	private String styleNo;
	private Integer noOfLaps;
	
}
