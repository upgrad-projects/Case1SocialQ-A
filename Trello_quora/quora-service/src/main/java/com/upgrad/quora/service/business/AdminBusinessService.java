package com.upgrad.quora.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@Service
public class AdminBusinessService {
	@Autowired
	private UserAuthDAO userAuthDao;
	
	@Autowired
	private UserDAO userDao;
	
	private static final String ROLE="admin";
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteUser(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		UserEntity userEntity = userDao.getUserById(userId);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out");
		}
		
		if(userEntity == null) {
			throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
		}
		
		if(userAuthEntity.getUser().getRole().equalsIgnoreCase(ROLE)) {
			userDao.deleteUser(userId);
			return userId;
		} else {
			throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
		}
	}
}
