package com.lanaVitor.Reservas.com.controllers.exceptions;

import com.lanaVitor.Reservas.com.services.exception.ExistingUserException;
import com.lanaVitor.Reservas.com.services.exception.NullEntityException;
import com.lanaVitor.Reservas.com.services.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

	private HttpStatus status;
	private StandardError err = new StandardError();
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		status = HttpStatus.NOT_FOUND;

		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(NullEntityException.class)
	public ResponseEntity<StandardError> entityNotFound(NullEntityException e, HttpServletRequest request) {
		status = HttpStatus.BAD_REQUEST;
		 err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler(ExistingUserException.class)
	public ResponseEntity<StandardError> entityNotFound(ExistingUserException e, HttpServletRequest request) {
		status = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> entityNotFound(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus statusValidationError = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError error = new ValidationError();
		error.setTimestamp(Instant.now());
		error.setStatus(statusValidationError.value());
		error.setError("Validation exception");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		// vai pegar os errors especificos na validação
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
				error.addError(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(statusValidationError).body(error);
	}
}
