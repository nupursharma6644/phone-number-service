package com.telco.phonenumberservice.service;

import com.telco.phonenumberservice.exception.IllegalStateException;
import com.telco.phonenumberservice.exception.NotFoundException;
import com.telco.phonenumberservice.repository.phonenumber.PhoneNumberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PhoneNumberServiceTest {

    @Autowired
    PhoneNumberService phoneNumberService;

    @MockitoSpyBean
    PhoneNumberRepository phoneNumberRepository;

    /**
     * Test Scenario: Fetch all phone numbers
     * Expected Outcome: Should return a list containing all phone numbers exists in the database
     */
    @Test
    void getAllPhoneNumbers_validRequest_shouldReturn_phoneNumberList() {
        List<String> phoneNumberList = phoneNumberService.getAllPhoneNumbers();

        verify(phoneNumberRepository, times(1)).findAll();

        assertNotNull(phoneNumberList);
        assertEquals(3, phoneNumberList.size());
        assertTrue(phoneNumberList.contains("1234567890"));
    }

    /**
     * Test Scenario: Fetch all phone numbers for a valid Customer
     * Expected Outcome: Should return a list containing all phone numbers linked to that Customer
     */
    @ParameterizedTest
    @CsvSource({
        "cust123, 2",
        "cust456, 1"
    })
    void getAllPhoneNumbersByCustomer_validRequest_shouldReturn_phoneNumberList(String customerId, int expectedPhoneNumbers) {
        List<String> phoneNumberList = phoneNumberService.getAllPhoneNumbersByCustomer(customerId);

        assertEquals(expectedPhoneNumbers, phoneNumberList.size());
    }

    /**
     * Test Scenario: Fetch all phone numbers for an invalid Customer, which does not exist
     * Expected Outcome: A 'NotFoundException' is thrown with expected error message
     */
    @Test
    void getAllPhoneNumbersByCustomer_invalidCustomer_shouldReturn_exception() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () ->
                phoneNumberService.getAllPhoneNumbersByCustomer("nonExistingCustomerId"));

        assertNotNull(notFoundException);
        assertEquals("Customer ID nonExistingCustomerId not found.", notFoundException.getMessage());
    }

    /**
     * Test Scenario: Fetch all phone numbers for a valid Customer, when no numbers linked to that customerId
     * Expected Outcome: A 'NotFoundException' is thrown with expected error message
     */
    @Test
    void getAllPhoneNumbersByCustomer_withNoNumbers_shouldReturn_exception() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () ->
                phoneNumberService.getAllPhoneNumbersByCustomer("cust000"));

        assertNotNull(notFoundException);
        assertEquals("No phone numbers linked to this customer", notFoundException.getMessage());
    }

    /**
     * Test Scenario: Activate the provided valid phone number
     * Expected Outcome: Should activate the given phone number and return the entity after successful activation
     */
    @ParameterizedTest
    @CsvSource({
            "1234567890",
            "5556665556"
    })
    void activatePhoneNumber_validRequest_shouldReturn_activated(String phoneNumber) {
        PhoneNumberResponseDto phoneNumberResponseDto =
                phoneNumberService.activatePhoneNumber(phoneNumber);

        assertEquals(true, phoneNumberResponseDto.isActive());
    }

    /**
     * Test Scenario: Activate the provided phone number, which does not exist
     * Expected Outcome: A 'NotFoundException' is thrown with expected error message
     */
    @Test
    void activatePhoneNumber_withInvalidNumber_shouldReturn_exception() {
        assertThatThrownBy(() -> phoneNumberService.activatePhoneNumber("000000000"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Phone Number not found.");
    }

    /**
     * Test Scenario: Activate the provided phone number, which is already activated
     * Expected Outcome: An 'IllegalStateException' is thrown with expected error message
     */
    @Test
    void activatePhoneNumber_withAlreadyActivatedNumber_shouldReturn_exception() {
        assertThatThrownBy(() -> phoneNumberService.activatePhoneNumber("9876543210"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Phone number is already activated");
    }

}
