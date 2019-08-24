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

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/")
public class AdminController {
	@Autowired
	private AdminBusinessService adminBusinessService;
	
	private static final String STATUS_DELETED = "USER SUCCESSFULLY DELETED";

	@RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId,
			@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

		String uuid = adminBusinessService.deleteUser(userId, authorization);
		
		UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(uuid).status(STATUS_DELETED);
		
		return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
	}
}
