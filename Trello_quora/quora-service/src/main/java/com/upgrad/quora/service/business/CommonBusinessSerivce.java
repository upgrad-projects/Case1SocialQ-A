package com.upgrad.quora.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@Service
public class CommonBusinessSerivce {	
	@Autowired
	private UserAuthDAO userAuthDao;
	
	@Autowired
	private UserDAO userDao;
	
	public UserEntity getuserbyId(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		UserEntity userEntity = userDao.getUserById(userId);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
		}
		
		if(userEntity == null) {
			throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
		}
		
		return userEntity;
	}
}
