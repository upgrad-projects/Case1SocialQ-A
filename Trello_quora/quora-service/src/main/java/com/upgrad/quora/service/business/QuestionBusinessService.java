package com.upgrad.quora.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;

@Service
public class QuestionBusinessService {
	@Autowired
	private QuestionDAO questionDao;
	
	@Autowired
	private UserAuthDAO userAuthDao;
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestionEntity createQuestion(QuestionEntity questionEntity) {
		return questionDao.createQuestion(questionEntity);		
	}
	
	public UserEntity getUserByAuthorization(final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
		}
		
		return userAuthEntity.getUser();
	}
	
	public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
		}
		
		return questionDao.getAllQuestions();
	}
}
