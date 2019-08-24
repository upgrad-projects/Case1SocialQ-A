package com.upgrad.quora.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.dao.QuestionDAO;
import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;

@Service
public class AnswerBusinessService {

	@Autowired
	private AnswerDAO answerDao;

	@Autowired
	private QuestionDAO questionDao;

	@Autowired
	private UserAuthDAO userAuthDao;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnswerEntity createAnswer(AnswerEntity answerEntity) {
		return answerDao.createAnswer(answerEntity);
	}

	public QuestionEntity getQuestion(final String questionId) throws InvalidQuestionException {
		QuestionEntity questionEntity = questionDao.getQuestion(questionId);

		if (questionEntity == null) {
			throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
		}

		return questionEntity;
	}

	public UserEntity getUser(final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		UserEntity userEntity = userAuthEntity.getUser();

		if (userEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
		}

		return userEntity;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnswerEntity editAnswer(AnswerEntity answerEntity, final String answerId, final String authorization)
			throws AnswerNotFoundException, AuthorizationFailedException {
		AnswerEntity answerToBeEdited = answerDao.getAnswer(answerId);
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);

		if (answerToBeEdited == null) {
			throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
		}

		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
		}

		if (userAuthEntity.getUser().getUuid().equalsIgnoreCase(answerToBeEdited.getUser().getUuid())) {
			answerToBeEdited.setAns(answerEntity.getAns());
			return answerDao.editAnswer(answerToBeEdited);
		} else {
			throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnswerEntity deleteAnswer(final String authorization, final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		AnswerEntity answerEntity = answerDao.getAnswer(answerId);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
		}
		
		if(answerEntity == null) {
			throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
		}
		
		if(userAuthEntity.getUser().getUuid().equalsIgnoreCase(answerEntity.getUser().getUuid())) {
			return answerDao.deleteAnswer(answerEntity);
		}
		else {
			throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
		}
	}
	
	public List<AnswerEntity> getAllAnswersForAQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);
		QuestionEntity questionEntity = questionDao.getQuestion(questionId);
		
		if(userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}
		
		if(userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
		}
		
		if(questionEntity == null) {
			throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
		}
		
		return answerDao.getAllAnswers(questionEntity);
	}
}
