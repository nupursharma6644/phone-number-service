package com.telco.phonenumberservice.controller;

import com.telco.phonenumberservice.service.PhoneNumberResponseDto;
import com.telco.phonenumberservice.service.PhoneNumberService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    @GetMapping(value = "/v{version}/phoneNumbers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<List<String>>> getAllPhoneNumbers(
            @RequestHeader final HttpHeaders httpHeaders
            ) {
        log.info("Fetching all phone numbers");
        return () -> new ResponseEntity<>(phoneNumberService.getAllPhoneNumbers(), HttpStatus.OK);
    }

    @GetMapping(value = "/v{version}/customers/{customerId}/phone-numbers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<List<String>>> getAllPhoneNumbersByCustomerId(
            @PathVariable(name = "customerId") final
            @Size(max = 8, message = "customerId length must be at most 8 characters")
            @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "customerId must be alphanumeric only")
            String customerId,
            @RequestHeader final HttpHeaders httpHeaders
    ) {
        log.info("Fetching phone numbers by customerId");
        return () -> ResponseEntity.ok(phoneNumberService.getAllPhoneNumbersByCustomer(customerId));
    }

    @PatchMapping(value = "/v{version}/phone-numbers/{phoneNumber}/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<PhoneNumberResponseDto>> activatePhoneNumber(
            @PathVariable(name = "phoneNumber")
            @Size(max = 20, message = "Phone number must be at most 20 characters")
            @Pattern(
                    regexp = "^[+]?\\d{1,3}?[- .()]?\\d+([- .()]?\\d+)*$",
                    message = "Invalid phone number format"
            )
            final String phoneNumber,
            @RequestHeader final HttpHeaders httpHeaders
    ) {
        log.info("Activating phone number");
        return () -> ResponseEntity.ok(phoneNumberService.activatePhoneNumber(phoneNumber));
    }


}
