package com.telco.phonenumberservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ApiError {
    private String errorId;
    private String errorMessage;
    private String informationLink;

    @JsonIgnore
    HttpStatus httpStatus;
}
