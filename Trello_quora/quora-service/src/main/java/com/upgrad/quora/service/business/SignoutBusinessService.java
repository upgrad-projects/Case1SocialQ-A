package com.upgrad.quora.service.business;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;

@Service
public class SignoutBusinessService {
	
	@Autowired
	UserAuthDAO userAuthDao;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserEntity signout(final String authorization) throws SignOutRestrictedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		
		if(userAuthEntity != null) {
			UserEntity userEntity = userAuthEntity.getUser();
			
			Timestamp logoutTime = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
			userAuthEntity.setLogoutAt(logoutTime);
			
			userAuthDao.update(userAuthEntity);
			
			return userEntity;
		}else {
			throw new SignOutRestrictedException("SGR-001","User is not Signed in");
		}
	}
}
