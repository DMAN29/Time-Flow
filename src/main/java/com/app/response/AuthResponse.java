	package com.app.response;
	
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class AuthResponse {
	
		private String email;
		
		private String jwt;
		
		private String msg;
	}