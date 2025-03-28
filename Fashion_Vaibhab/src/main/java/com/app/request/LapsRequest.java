package com.app.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LapsRequest {

	private OperatorRequest operator;
	private List<Long> lapsMS;
}
