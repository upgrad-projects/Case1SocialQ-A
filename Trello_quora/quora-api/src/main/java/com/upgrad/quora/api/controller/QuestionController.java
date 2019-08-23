package com.upgrad.quora.api.controller;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.dao.UserAuthDAO;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;

@RestController
@RequestMapping("/")
public class QuestionController {
	@Autowired
	private QuestionBusinessService questionService;
	
	private static final String STATUS = "QUESTION CREATED";
	
	@RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType
			.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest question, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
		QuestionEntity questionEntity = new QuestionEntity();
		UserEntity userEntity = questionService.getUserByAuthorization(authorization);
		
		questionEntity.setUuid(UUID.randomUUID().toString());
		questionEntity.setContent(question.getContent());
		questionEntity.setDate(Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
		questionEntity.setUser(userEntity);
		
		QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity);
		QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid())
																  .status(STATUS);
		
		return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
	}
}
