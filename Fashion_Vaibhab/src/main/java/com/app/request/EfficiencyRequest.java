package com.app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EfficiencyRequest {

	private String styleNo;
	private Double efficiency;
}
