package com.upgrad.quora.api.controller;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@RestController
@RequestMapping("/")
public class QuestionController {
	@Autowired
	private QuestionBusinessService questionService;

	private static final String STATUS_CREATED = "QUESTION CREATED";
	private static final String STATUS_EDITED = "QUESTION EDITED";
	private static final String STATUS_DELETED = "QUESTION DELETED";

	// Creates a question and maps it to appropriate user.
	// Throws appropriate exception when user not logged in or found.
	@RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest question,
			@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
		QuestionEntity questionEntity = new QuestionEntity();
		UserEntity userEntity = questionService.getUserByAuthorization(authorization);

		questionEntity.setUuid(UUID.randomUUID().toString());
		questionEntity.setContent(question.getContent());
		questionEntity.setDate(Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
		questionEntity.setUser(userEntity);

		QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity);
		QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid())
				.status(STATUS_CREATED);

		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
	}

	// Gets all questions asked by any user.
	// Throws appropriate exception when user not logged in or found.
	@RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
			@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
		List<QuestionEntity> allQuestionsList = questionService.getAllQuestions(authorization);
		List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<QuestionDetailsResponse>();

		if (allQuestionsList != null) {
			for (QuestionEntity que : allQuestionsList) {
				questionDetailsResponses.add(new QuestionDetailsResponse().id(que.getUuid()).content(que.getContent()));
			}
		}

		return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
	}

	// Edits the question.
	// Verifies if the question is edited by the same person who has created this
	// question.
	// Throws appropriate exception if user is not logged in, not found or tries to
	// edit someone else's question.
	@RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<QuestionEditResponse> editQuestionContent(QuestionEditRequest questionEditContent,
			@PathVariable("questionId") final String questionId,
			@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

		QuestionEntity questionEntity = new QuestionEntity();

		questionEntity.setContent(questionEditContent.getContent());

		QuestionEntity editedQuestionEntity = questionService.editQuestion(questionEntity, questionId, authorization);

		QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editedQuestionEntity.getUuid())
				.status(STATUS_EDITED);

		return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
	}

	// Delete the question
	// Verifies if the question is deleted by the same person who has created this
	// question.
	// Throws appropriate exception if user is not logged in, not found or tries to
	// delete someone else's question.
	@RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId,
			@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

		QuestionEntity questionEntity = questionService.deleteQuestion(questionId, authorization);

		QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionEntity.getUuid())
				.status(STATUS_DELETED);

		return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
	}

	// Get all questions posted by a particular user. This can be queried by any
	// user.
	// Throw appropriate errors when the user is not logged in, signed out or the
	// user does not exist in the database.
	@RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByUserId(
			@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization)
			throws AuthorizationFailedException, UserNotFoundException {
		List<QuestionEntity> allQuestions = questionService.getAllQuestionsByUserId(authorization, userId);

		List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<QuestionDetailsResponse>();

		for (QuestionEntity que : allQuestions) {
			questionDetailsResponses.add(new QuestionDetailsResponse().id(que.getUuid()).content(que.getContent()));
		}

		return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
	}
}
