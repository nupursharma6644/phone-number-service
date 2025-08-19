package com.telco.phonenumberservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class PhoneNumberServiceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(Exception exception) {
        log.error("Resource not found: {}", exception.getMessage());

        ApiError apiError = ApiError.builder()
                .errorId(HttpStatus.NOT_FOUND.getReasonPhrase())
                .errorMessage(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler({IllegalStateException.class, ConstraintViolationException.class, UnexpectedTypeException.class})
    public ResponseEntity<ApiError> handleIllegalStateException(Exception exception) {
        log.error("Bad Request: {}", exception.getMessage());

        ApiError apiError = ApiError.builder()
                .errorId(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorMessage(exception.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleGenericException(Exception exception) {
        log.error("Exception: {}", exception.getMessage());

        ApiError apiError = ApiError.builder()
                .errorId(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errorMessage("Something went wrong")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
