package com.upgrad.quora.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private SignupBusinessService signupBusinessService;

	@RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignupUserResponse> signUp(SignupUserRequest userRequest) {
		final UserEntity userEntity = new UserEntity();
		
		userEntity.setUuid(UUID.randomUUID().toString());
		userEntity.setFirstname(userRequest.getFirstName());
		userEntity.setLastname(userRequest.getLastName());
		userEntity.setUsername(userRequest.getUserName());
		userEntity.setEmail(userRequest.getEmailAddress());
		userEntity.setPassword(userRequest.getPassword());
		userEntity.setCountry(userRequest.getCountry());
		userEntity.setAboutme(userRequest.getAboutMe());
		userEntity.setDob(userRequest.getDob());
		userEntity.setContactnumber(userRequest.getContactNumber());
		
		signupBusinessService.signup(userEntity);
		
		return new ResponseEntity<SignupUserResponse>(HttpStatus.OK);
	}
}
