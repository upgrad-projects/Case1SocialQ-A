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
	
	private static String ROLE = "admin";

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestionEntity createQuestion(QuestionEntity questionEntity) {
		return questionDao.createQuestion(questionEntity);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserEntity getUserByAuthorization(final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);

		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
		}

		return userAuthEntity.getUser();
	}

	public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);

		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002",
					"User is signed out. Sign in first to get all questions");
		}

		return questionDao.getAllQuestions();
	}

	public QuestionEntity editQuestion(QuestionEntity questionEntity, final String questionId,
			final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);

		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002",
					"User is signed out. Sign in first to edit the question");
		}

		QuestionEntity questionToBeEdited = questionDao.getQuestion(questionId);
		UserEntity userEntity = userAuthEntity.getUser();

		if (questionToBeEdited.getUser().getUuid().equals(userEntity.getUuid())) {
			questionEntity.setAnswers(questionToBeEdited.getAnswers());
			questionEntity.setDate(questionToBeEdited.getDate());
			questionEntity.setId(questionToBeEdited.getId());
			questionEntity.setUser(questionToBeEdited.getUser());
			questionEntity.setUuid(questionToBeEdited.getUuid());

			return questionDao.editQuestion(questionEntity);
		} else {
			throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
		}
	}

	public QuestionEntity deleteQuestion(final String questionId, final String authorization) throws AuthorizationFailedException {
		UserAuthEntity userAuthEntity = userAuthDao.getUserByAuthorization(authorization);

		if (userAuthEntity == null) {
			throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
		}

		if (userAuthEntity.getLogoutAt() != null) {
			throw new AuthorizationFailedException("ATHR-002",
					"User is signed out. Sign in first to delete the question");
		}
		
		QuestionEntity questionToBeDeleted = questionDao.getQuestion(questionId);
		UserEntity userEntity = userAuthEntity.getUser();

		if (questionToBeDeleted.getUser().getUuid().equals(userEntity.getUuid()) || 
				userEntity.getRole().equalsIgnoreCase(ROLE)) {
			return questionDao.deleteQuestion(questionToBeDeleted);
		} else {
			throw new AuthorizationFailedException("ATHR-003", "Only the question owner or the admin can delete the question");
		}
	}
}
