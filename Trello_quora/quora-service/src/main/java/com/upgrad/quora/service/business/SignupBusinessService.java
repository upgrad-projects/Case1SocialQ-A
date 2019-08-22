package com.upgrad.quora.service.business;

import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.UserDAO;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;

@Service
public class SignupBusinessService {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private PasswordCryptographyProvider cryptographyProvider;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
		String[] encryptedText =cryptographyProvider.encrypt(userEntity.getPassword());
		UserEntity createdUserEntity = null;
		
		userEntity.setSalt(encryptedText[0]);
		userEntity.setPassword(encryptedText[1]);
		
		try {
			createdUserEntity = userDao.createUser(userEntity);
		}catch (PersistenceException pe) {
			ConstraintViolationException cve = (ConstraintViolationException)pe.getCause();
			String constraintName = cve.getConstraintName();
			
			if(constraintName.equals("users_username_key")) {
				throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken.");
			}
			if(constraintName.equals("users_email_key")) {
				throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId.");
			}
			
		}
		
		return createdUserEntity;
	}
}
