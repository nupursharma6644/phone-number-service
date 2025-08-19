package com.telco.phonenumberservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
