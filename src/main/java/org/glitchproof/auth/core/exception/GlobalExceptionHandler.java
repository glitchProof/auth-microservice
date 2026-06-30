package org.glitchproof.auth.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import jakarta.validation.ConstraintViolationException;
import org.glitchproof.auth.core.enums.DomainErrorCodes;
import org.glitchproof.auth.core.dto.ApiExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        var errorResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .violations(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Method level validation of jakarta
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);

            errors.put(field, violation.getMessage());
        });

        var errorResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .violations(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiExceptionResponse> handleDomainExceptions(DomainException ex) {
        var errorResponse = ApiExceptionResponse.builder()
                .code(ex.getCode())
                .detail(ex.getDetail())
                .timestamp(LocalDateTime.now())
                .violations(ex.getViolations())
                .status(ex.getStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Data integrity exception for db throw constraints exceptions
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var errorResponse = ApiExceptionResponse.builder()
                .code(DomainErrorCodes.SOMETHING_WENT_WRONG.getValue())
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // For external bad request exceptions
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ApiExceptionResponse> handleUsernameNotFoundException(Exception ex) {
        var errorResponse =  ApiExceptionResponse.builder()
                .code(DomainErrorCodes.AUTH_EMAIL_TAKEN.getValue())
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();


        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
        var errorResponse =  ApiExceptionResponse.builder()
                .code(10001)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("Something went wrong")
                .timestamp(LocalDateTime.now())
                .build();

        log.error("Something went wrong", ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
