package com.revature.movietn.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.revature.movietn.utils.custom_exceptions.UnauthorizedAccessException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;

import com.revature.movietn.utils.custom_exceptions.BadRequestException;
import com.revature.movietn.utils.custom_exceptions.ResourceConflictException;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class ExceptionController {

    /**
     * This exception handler handles exceptions when a resource
     * is not found from the database.
     * 
     * @param e the ResourceNotFoundException
     * @return the ResponseEntity object with a status of 502 - BAD_GATEWAY and
     *         a body with the timestamp and exception message.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resBody);
    }

    /**
     * This exception handler handles resource conflicts
     * (e.g. request to register duplicate user).
     * 
     * @param e the ResourceConflictException
     * @return the ResponseEntity object with a status of 409 - CONFLICT and
     *         a body with the timestamp and exception message.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> resourceConflictExceptionHandler(ResourceConflictException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resBody);
    }

    /**
     * This exception handler handles unauthorized requests.
     * 
     * @param e the InvalidCredentialsException
     * @return the ResponseEntity object with a status of 401 - UNAUTHORIZED and
     *         a body with the timestamp and exception message.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> unauthorizedAccessExceptionHandler(UnauthorizedAccessException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resBody);
    }

    /**
     * This exception handler handles all errors relating to missing field data
     * in a request.
     * 
     * @param e the MethodArgumentNotValidException
     * @return the ResponseEntity object with a status of 400 - BAD_REQUEST and
     *         a body with the timestamp and error messages for each field.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {
        Map<String, Object> resBody = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fe) -> {
            String fieldName = fe.getField();
            String errorMessage = fe.getDefaultMessage();
            resBody.put(fieldName, errorMessage);
        });
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
    }

    /**
     * This exception handler handles all exceptions relating to all invalid
     * paramters. This includes path variables and request params.
     * 
     * @param e the ConstraintViolationException
     * @return the ResponseEntity object with a status of 400 - BAD_REQUEST and
     *         a body with the timestamp and error messages for each field.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> constraintViolationException(
            ConstraintViolationException e) {
        Map<String, Object> resBody = new HashMap<>();
        e.getConstraintViolations().forEach((cv) -> {
            String field = null;
            for (Node node : cv.getPropertyPath()) {
                field = node.getName();
            }
            resBody.put(field, cv.getMessage());
        });
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
    }

    /**
     * This exception handler handles all errors relating to bad requests such as
     * invalid combination of data.
     * 
     * @param e the BadRequestException
     * @return the ResponseEntity object with a status of 400 - BAD_REQUEST and
     *         a body with the timestamp and message.
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> badRequestExceptionHandler(BadRequestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("timestamp", new Date(System.currentTimeMillis()));
        resBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody);
    }
}
