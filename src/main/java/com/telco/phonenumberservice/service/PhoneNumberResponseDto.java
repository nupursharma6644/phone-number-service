package com.telco.phonenumberservice.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PhoneNumberResponseDto {
    private String phoneNumber;
    private boolean isActive;
}
