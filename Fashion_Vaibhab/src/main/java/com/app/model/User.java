package com.app.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

	@Id
	private String id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<Role> role;
	private String password;
	private LocalDateTime createdAt;
	
}
