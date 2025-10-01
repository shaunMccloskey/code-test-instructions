package com.urlshortener.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  public static final String ERROR_STR = "error";

  @ExceptionHandler(AliasAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleAliasAlreadyExists(
      AliasAlreadyExistsException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_STR, ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(AliasNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleAliasNotFound(AliasNotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_STR, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_STR, "An unexpected error occurred: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
