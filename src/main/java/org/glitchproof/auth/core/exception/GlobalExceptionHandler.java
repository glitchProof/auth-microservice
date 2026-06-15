package org.glitchproof.auth.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.glitchproof.auth.core.dto.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    // For external bad request exceptions
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<ApiExceptionResponse> handleUsernameNotFoundException(Exception ex) {
        var errorResponse =  ApiExceptionResponse.builder()
                .code(10001)
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
                .build();

        log.error("Something went wrong {}", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
