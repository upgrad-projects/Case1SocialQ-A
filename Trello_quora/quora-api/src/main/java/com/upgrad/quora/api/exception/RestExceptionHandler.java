package com.upgrad.quora.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;

@ControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupException(SignUpRestrictedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }
	
	@ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> signinException(AuthorizationFailedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }
	
	@ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signoutException(SignOutRestrictedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.FORBIDDEN
        );
    }
}
