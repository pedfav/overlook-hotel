package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.exceptions.EmailAlreadyTakenException;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.http.datacontracts.ErrorDataContract;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={ ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorDataContract errorDataContract = ErrorDataContract.builder()
                .message(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, errorDataContract, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value={ EmailAlreadyTakenException.class })
    protected ResponseEntity<Object> handleConstraintViolation(RuntimeException ex, WebRequest request) {
        ErrorDataContract errorDataContract = ErrorDataContract.builder()
                .message(ex.getMessage())
                .build();

        return handleExceptionInternal(ex, errorDataContract, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorDataContract errorDataContract = ErrorDataContract.builder()
                .message("Error on validating body!")
                .errors(errors)
                .build();

        return handleExceptionInternal(ex, errorDataContract, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value={ Exception.class })
    protected ResponseEntity<Object> handleConstraintViolation(Exception ex, WebRequest request) {
        ErrorDataContract errorDataContract = ErrorDataContract.builder()
                .message("Something went wrong, please try again!")
                .build();

        return handleExceptionInternal(ex, errorDataContract, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
