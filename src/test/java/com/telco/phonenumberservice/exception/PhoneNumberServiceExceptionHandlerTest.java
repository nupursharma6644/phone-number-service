package com.telco.phonenumberservice.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhoneNumberServiceExceptionHandlerTest {

    private PhoneNumberServiceExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new PhoneNumberServiceExceptionHandler();
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFoundResponse() {
        NotFoundException ex = new NotFoundException("Customer not found");
        ResponseEntity<ApiError> response = exceptionHandler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found", response.getBody().getErrorMessage());
        assertEquals("Not Found", response.getBody().getErrorId());
    }

    @Test
    void handleIllegalStateException_WithIllegalStateException_ShouldReturnBadRequest() {
        IllegalStateException ex = new IllegalStateException("Invalid state");
        ResponseEntity<ApiError> response = exceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid state", response.getBody().getErrorMessage());
        assertEquals("Bad Request", response.getBody().getErrorId());
    }

    @Test
    void handleIllegalStateException_WithConstraintViolationException_ShouldReturnBadRequest() {
        ConstraintViolationException ex = new ConstraintViolationException(null);
        ResponseEntity<ApiError> response = exceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getErrorId());
    }

    @Test
    void handleIllegalStateException_WithUnexpectedTypeException_ShouldReturnBadRequest() {
        UnexpectedTypeException ex = new UnexpectedTypeException("Unexpected type");
        ResponseEntity<ApiError> response = exceptionHandler.handleIllegalStateException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unexpected type", response.getBody().getErrorMessage());
        assertEquals("Bad Request", response.getBody().getErrorId());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        ResponseEntity<ApiError> response = exceptionHandler.handleGenericException(new RuntimeException("Something went wrong"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().getErrorMessage());
        assertEquals("Internal Server Error", response.getBody().getErrorId());
    }
}
