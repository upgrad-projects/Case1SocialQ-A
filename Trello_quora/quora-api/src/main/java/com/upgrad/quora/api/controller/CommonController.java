package com.upgrad.quora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/")
public class CommonController {
	@Autowired
	private CommonBusinessService commonBusinessService;

	// Fetches the userprofile of the logged in user.
	// if the user is not logged in, it raises appropriate error messages.
	@RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String userId,
			@RequestHeader("authorization") final String authorization)
			throws AuthorizationFailedException, UserNotFoundException {
		UserEntity userEntity = commonBusinessService.getuserbyId(userId, authorization);

		UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstname())
				.lastName(userEntity.getLastname()).userName(userEntity.getUsername())
				.emailAddress(userEntity.getEmail()).country(userEntity.getCountry()).aboutMe(userEntity.getAboutme())
				.dob(userEntity.getDob()).contactNumber(userEntity.getContactnumber());

		return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
	}
}