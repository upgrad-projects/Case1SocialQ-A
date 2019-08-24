package com.upgrad.quora.api.controller;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SigninBusinessService;
import com.upgrad.quora.service.business.SignoutBusinessService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private SignupBusinessService signupBusinessService;
	
	@Autowired
	private SigninBusinessService signinBusinessService;
	
	@Autowired 
	SignoutBusinessService signoutBusinessService;
	
	private static final String MESSAGE_REGISTERED = "USER SUCCESSFULLY REGISTERED";
	private static final String MESSAGE_SIGNEDIN = "SIGNED IN SUCCESSFULLY";
	private static final String MESSAGE_SIGNEDOUT = "SIGNED OUT SUCCESSFULLY";

	//This method deserializes json to create a new user who is trying to signup.
	//Upon success returns the Uuid of the user created. Else triggers appropriate exceptions.
	@RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignupUserResponse> signUp(SignupUserRequest userRequest) throws SignUpRestrictedException {
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
		userEntity.setRole("nonadmin");
		userEntity.setContactnumber(userRequest.getContactNumber());
		
		UserEntity createdUserEntity = signupBusinessService.signup(userEntity);
		
		final SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status(MESSAGE_REGISTERED);
		
		return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
	}
	
	//This method manages the authentication for signin. It requires the user to combine username:password in base64 encoding.
	//This is processed and once verified, the user is returned a uuid and status.
	@RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
		byte[] decodedAuthorization = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
		
		String decodedValue = new String(decodedAuthorization);
		
		String[] decodedArray = decodedValue.split(":");
		
		UserAuthEntity userAuthEntity = signinBusinessService.signin(decodedArray[0], decodedArray[1]);
		UserEntity userEntity = userAuthEntity.getUser();
		
		SigninResponse signinResponse = new SigninResponse().id(UUID.fromString(userEntity.getUuid()).toString())
															.message(MESSAGE_SIGNEDIN);
		
		HttpHeaders header = new HttpHeaders();
		header.add("access-token", userAuthEntity.getAccessToken());
		return new ResponseEntity<SigninResponse>(signinResponse, header, HttpStatus.OK);
		
		
	}

	//This method verifies the user if he or she is signed in.
	//If verified positive, then the user is signed out by updating the loggedout time.
	//Else appropriate error handling is triggerd
	@RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
		UserEntity userEntity = signoutBusinessService.signout(authorization);
		
		SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid()).message(MESSAGE_SIGNEDOUT);
		
		return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
	}
}
