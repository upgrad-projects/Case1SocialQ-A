package com.upgrad.quora.service.business;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;

@Service
public class SigninBusinessService {
	@Autowired
	private UserDAO userDao;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
	public UserAuthEntity signin(final String email, final String password) throws AuthorizationFailedException {
		UserEntity userEntity = userDao.getUserByEmail(email);
		
		if(userEntity == null) {
			throw new AuthorizationFailedException("ATH-001", "This username does not exist.");
		}
		
		final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());
		
		if(encryptedPassword.equals(userEntity.getPassword())) {
			UserAuthEntity userAuthEntity = new UserAuthEntity();
			JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			
			userAuthEntity.setUser(userEntity);
			
			ZonedDateTime issuedAt = ZonedDateTime.now();
			Timestamp tIssuedAt = Timestamp.valueOf(issuedAt.toLocalDateTime());
			ZonedDateTime expiresAt = issuedAt.plusHours(8);
			Timestamp tExpiresAt = Timestamp.valueOf(expiresAt.toLocalDateTime());
			String accessToken = jwtTokenProvider.generateToken(userEntity.getUuid(), issuedAt, expiresAt);
			
			userAuthEntity.setAccessToken(accessToken);
			userAuthEntity.setExpiresAt(tExpiresAt);
			userAuthEntity.setLoginAt(tIssuedAt);
			userAuthEntity.setUser(userEntity);
			userAuthEntity.setUuid(userEntity.getUuid());
			
			return userDao.createUserAuthToken(userAuthEntity);			
		} else {
			throw new AuthorizationFailedException("ATH-002", "Password failed");
		}
	}
}
