package com.prp.tickets.controller;


import com.prp.tickets.domain.dto.ErrorDto;
import com.prp.tickets.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
	log.error("Caught ConstraintViolationException", ex);
	String errorMessage = ex
							.getConstraintViolations()
							.stream()
							.findFirst()
							.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
							.orElse("A constraint violation occurred");
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError(errorMessage);
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
	MethodArgumentNotValidException ex) {
	log.error("Caught MethodArgumentNotValidException", ex);
	ErrorDto errorDto = new ErrorDto();
	BindingResult bindingResult = ex.getBindingResult();
	List<FieldError> fieldErrors = bindingResult.getFieldErrors();
	String errorMessage = fieldErrors
							.stream()
							.findFirst()
							.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
							.orElse("Validation error occurred");
	errorDto.setError(errorMessage);
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorDto> handleException(Exception exception) {
	log.error("Error details: {}", exception.getMessage());
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("An unknown error occurred " + exception.getMessage());
	return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
	log.error("Caught UserNotFoundException", ex);
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("User not found");
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  
  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException ex) {
	log.error("Caught EventNotFoundException", ex);
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("Event not found");
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(TicketTypeNotFoundException.class)
  public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException ex) {
	log.error("Caught TicketTypeNotFoundException", ex);
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("Ticket Type not found");
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(EventUpdateException.class)
  public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException ex) {
	log.error("Caught EventUpdateException", ex);
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("Unable to update event");
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(QrCodeGenerationException.class)
  public ResponseEntity<ErrorDto> handleQrCodeGenerationException(QrCodeGenerationException ex) {
	log.error("Caught QrCodeGenerationException", ex);
	ErrorDto errorDto = new ErrorDto();
	errorDto.setError("Unable to generate qrcode");
	return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }
  
}
