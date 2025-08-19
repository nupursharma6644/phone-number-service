package com.telco.phonenumberservice.service;

import com.telco.phonenumberservice.exception.IllegalStateException;
import com.telco.phonenumberservice.exception.NotFoundException;
import com.telco.phonenumberservice.repository.customer.CustomerRepository;
import com.telco.phonenumberservice.repository.phonenumber.PhoneNumber;
import com.telco.phonenumberservice.repository.phonenumber.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    private final CustomerRepository customerRepository;

    /**
     * This method fetches all PhoneNumber entities from the repository,
     * extracts their number field, and returns a list of phone number strings.
     *
     * @return a list of all phone numbers as Strings
     */
    public List<String> getAllPhoneNumbers() {
        return phoneNumberRepository.findAll()
                .stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all phone numbers associated with a specific customer.
     *
     * This method first checks if a customer with the given customerId exists.
     * If the customer does not exist, it throws a NotFoundException.
     * It then fetches all PhoneNumber entities linked to the customer.
     * If no phone numbers are found, it throws a NotFoundException.
     * Finally, it returns a list of phone number strings.
     *
     * @param customerId the unique identifier of the customer
     * @return a list of phone numbers as Strings associated with the given customer
     * @throws NotFoundException if the customer does not exist or has no phone numbers
     */
    public List<String> getAllPhoneNumbersByCustomer(String customerId) {
        if (!customerRepository.existsByCustomerId(customerId)) {
            throw new NotFoundException("Customer ID "+customerId+" not found.");
        }

        List<PhoneNumber> phoneNumbers = phoneNumberRepository.findByCustomer_CustomerId(customerId);
        if (phoneNumbers.isEmpty()) {
            throw new NotFoundException("No phone numbers linked to this customer");
        }
        return phoneNumbers.stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.toList());
    }

    /**
     * Activates a given phone number.
     *
     * This method searches for a PhoneNumber entity matching the provided phoneNumber.
     * If the phone number is not found, it throws a NotFoundException.
     * If the phone number is already active, it throws an IllegalStateException.
     * Otherwise, it marks the phone number as active, saves the change to the repository,
     * and returns a PhoneNumberResponseDto containing the phone number and its active status.
     *
     * @param phoneNumber the phone number to activate
     * @return a PhoneNumberResponseDto containing the phone number and its active status
     * @throws NotFoundException if the phone number does not exist in the database
     * @throws IllegalStateException if the phone number is already active
     */
    public PhoneNumberResponseDto activatePhoneNumber(String phoneNumber) {
        PhoneNumber phoneNumberEntity = phoneNumberRepository.findByNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("Phone Number not found."));

        if(phoneNumberEntity.isActive()) {
            throw new IllegalStateException("Phone number is already activated");
        }

        phoneNumberEntity.setActive(true);
        phoneNumberRepository.save(phoneNumberEntity);

        return PhoneNumberResponseDto.builder()
                .phoneNumber(phoneNumberEntity.getNumber())
                .isActive(phoneNumberEntity.isActive())
                .build();
    }
}
