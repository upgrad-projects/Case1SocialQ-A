package com.upgrad.quora.api.controller;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.fabric.xmlrpc.base.Array;
import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;

@RestController
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private AnswerBusinessService answerBusinessService;

	private static final String STATUS_CREATED = "ANSWER CREATED";
	private static final String STATUS_EDITED = "ANSWER EDITED";
	private static final String STATUS_DELETED = "ANSWER DELETED";

	// create an answer to the question
	// throw errors if the question does not exist or user is not logged in or
	// signed out
	@RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerResponse> createAnswer(AnswerRequest answerRequest,
			@PathVariable("questionId") final String questionId,
			@RequestHeader("authorization") final String authorization)
			throws InvalidQuestionException, AuthorizationFailedException {
		AnswerEntity answerEntity = new AnswerEntity();
		QuestionEntity questionEntity = answerBusinessService.getQuestion(questionId);
		UserEntity userEntity = answerBusinessService.getUser(authorization);

		answerEntity.setQuestion(questionEntity);
		answerEntity.setAns(answerRequest.getAnswer());
		answerEntity.setDate(Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
		answerEntity.setUser(userEntity);
		answerEntity.setUuid(UUID.randomUUID().toString());

		AnswerEntity createdAnswer = answerBusinessService.createAnswer(answerEntity);

		AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status(STATUS_CREATED);

		return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);
	}

	// edit an already existing answer
	// throw errors when the user is not signed in, logged out, someone other than
	// the creator of the answer tries to edit the answer or the answer does not
	// exist
	@RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerEditResponse> editAnswerContent(AnswerEditRequest answerEditRequest,
			@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization)
			throws AnswerNotFoundException, AuthorizationFailedException {
		AnswerEntity answerEntity = new AnswerEntity();

		answerEntity.setAns(answerEditRequest.getContent());

		AnswerEntity editedAnswerEntity = answerBusinessService.editAnswer(answerEntity, answerId, authorization);

		AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(editedAnswerEntity.getUuid())
				.status(STATUS_EDITED);

		return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
	}

	// delete an answer
	// throw errors if user not signed in, logged out, answer does not exist or
	// anyone other than the owner of the answer or the admin tries to delete the
	// answer
	@RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId,
			@RequestHeader("authorization") final String authorization)
			throws AuthorizationFailedException, AnswerNotFoundException {
		AnswerEntity answerEntity = answerBusinessService.deleteAnswer(authorization, answerId);

		AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerEntity.getUuid())
				.status(STATUS_DELETED);

		return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.CREATED);
	}

	// get all answers to a specific question
	// throw errors when user is not signed in or logged out or question does not
	// exist
	@RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
			@PathVariable("questionId") final String questionId,
			@RequestHeader("authorization") final String authorization)
			throws AuthorizationFailedException, InvalidQuestionException {
		List<AnswerEntity> allAnswers = answerBusinessService.getAllAnswersForAQuestion(questionId, authorization);
		List<AnswerDetailsResponse> listAnswerDetailsResponses = new ArrayList<AnswerDetailsResponse>();

		for (AnswerEntity ans : allAnswers) {
			listAnswerDetailsResponses.add(new AnswerDetailsResponse().id(ans.getUuid()).answerContent(ans.getAns()));
		}

		return new ResponseEntity<List<AnswerDetailsResponse>>(listAnswerDetailsResponses, HttpStatus.OK);
	}
}