package com.telco.phonenumberservice.controller;

import com.telco.phonenumberservice.exception.IllegalStateException;
import com.telco.phonenumberservice.exception.NotFoundException;
import com.telco.phonenumberservice.service.PhoneNumberResponseDto;
import com.telco.phonenumberservice.service.PhoneNumberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneNumberController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PhoneNumberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private PhoneNumberService phoneNumberService;

    private static String GET_ALL_PHONE_NUMBERS_PATH = "/v{version}/phoneNumbers";
    private static String GET_ALL_PHONE_NUMBERS_BY_CUSTOMER_PATH = "/v{version}/customers/{customerId}/phone-numbers";
    private static String ACTIVATE_PHONE_NUMBER_PATH = "/v{version}/phone-numbers/{phoneNumber}/activate";

    /**
     * Test for the GET /v1/phoneNumbers endpoint.
     *
     * This test verifies that when the PhoneNumberService returns a non-empty list of phone numbers,
     * the controller responds with:
     * - HTTP Status 200 OK
     * - A JSON array as the response body
     * - The array contains at least one phone number
     */
    @Test
    void getAllPhoneNumbers_validRequest_returnSuccess() throws Exception {
        when(phoneNumberService.getAllPhoneNumbers())
                .thenReturn(List.of("1234567890", "9876543210", "5556665556"));

        MvcResult mvcResult = getMvcResult(get(GET_ALL_PHONE_NUMBERS_PATH, 1));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    /**
     * Test for the GET /v1/customers/{customerId}/phone-numbers endpoint.
     *
     * This test verifies that when the PhoneNumberService returns a non-empty list of phone numbers,
     * the controller responds with:
     * - HTTP Status 200 OK
     * - A JSON array as the response body
     * - The array contains at least one phone number
     */
    @Test
    void getAllPhoneNumbersByCustomer_validRequest_returnSuccess() throws Exception {
        String testCustomerId = "cust123";
        when(phoneNumberService.getAllPhoneNumbersByCustomer(testCustomerId))
                .thenReturn(List.of("1234567890", "9876543210"));

        MvcResult mvcResult = getMvcResult(get(GET_ALL_PHONE_NUMBERS_BY_CUSTOMER_PATH, 1, testCustomerId));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(equalTo(2)));
    }

    /**
     * Test for the GET /v1/customers/{customerId}/phone-numbers endpoint.
     *
     * This test verifies that when the PhoneNumberService throws exception due to CustomerNotFound or NoPhoneNumbersLinkedToCustomer,
     * the controller responds with:
     * - HTTP Status 404 NOT FOUND
     * - A JSON object as the Error response body
     * - The response contains expected error details
     */
    @ParameterizedTest
    @CsvSource({
            "telco11, Customer Id not found",
            "telco222, No phone numbers linked to this customer"
    })
    void getAllPhoneNumbersByCustomer_customerNotFound_returnErrorResponse(String customerId, String errorMessage) throws Exception {
        when(phoneNumberService.getAllPhoneNumbersByCustomer(customerId))
                .thenThrow(new NotFoundException(errorMessage));

        MvcResult mvcResult = getMvcResult(get(GET_ALL_PHONE_NUMBERS_BY_CUSTOMER_PATH, 1, customerId));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorId").value("Not Found"))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage));
    }

    /**
     * Test for the PATCH /v1/phone-numbers/{phoneNumber}/activate endpoint.
     *
     * This test verifies that when the PhoneNumberService activates the number successfully,
     * the controller responds with:
     * - HTTP Status 200 OK
     * - A JSON Object as the response body
     * - The response contains the details of successfully activated phone number
     */
    @Test
    void activatePhoneNumber_validRequest_returnSuccess() throws Exception {
        String testPhoneNumber = "1234567890";
        when(phoneNumberService.activatePhoneNumber(testPhoneNumber))
                .thenReturn(PhoneNumberResponseDto.builder()
                        .isActive(true)
                        .phoneNumber(testPhoneNumber)
                        .build());

        MvcResult mvcResult = getMvcResult(patch(ACTIVATE_PHONE_NUMBER_PATH, 1, testPhoneNumber));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value(testPhoneNumber))
                .andExpect(jsonPath("$.active").value(true));
    }

    /**
     * Test for the PATCH /v1/phone-numbers/{phoneNumber}/activate endpoint.
     *
     * This test verifies that when the PhoneNumberService throws exception due to phone-Number-Not-Found,
     * the controller responds with:
     * - HTTP Status 404 NOT FOUND
     * - A JSON object as the Error response body
     * - The response contains expected error details
     */
    @Test
    void activatePhoneNumber_phoneNumberNotFound_returnErrorResponse() throws Exception {
        String nonExistingPhoneNumber = "0000000000";
        when(phoneNumberService.activatePhoneNumber(nonExistingPhoneNumber))
                .thenThrow(new NotFoundException("Phone Number not found."));

        MvcResult mvcResult = getMvcResult(patch(ACTIVATE_PHONE_NUMBER_PATH, 1, nonExistingPhoneNumber));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorId").value("Not Found"))
                .andExpect(jsonPath("$.errorMessage").value("Phone Number not found."));
    }

    /**
     * Test for the PATCH /v1/phone-numbers/{phoneNumber}/activate endpoint.
     *
     * This test verifies that when the PhoneNumberService throws exception due to phone-Number-Already-Activated,
     * the controller responds with:
     * - HTTP Status 400 BAD REQUEST
     * - A JSON object as the Error response body
     * - The response contains expected error details
     */
    @Test
    void activatePhoneNumber_phoneNumberAlreadyActivated_returnErrorResponse() throws Exception {
        String alreadyActivatedPhoneNumber = "9876543210";
        when(phoneNumberService.activatePhoneNumber(alreadyActivatedPhoneNumber))
                .thenThrow(new IllegalStateException("Phone number is already activated"));

        MvcResult mvcResult = getMvcResult(patch(ACTIVATE_PHONE_NUMBER_PATH, 1, alreadyActivatedPhoneNumber));
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("Phone number is already activated"));
    }

    /**
     * Test for the PATCH /v1/phone-numbers/{phoneNumber}/activate endpoint.
     *
     * This test verifies that when the API is called with invalid phoneNumber format
     * the controller responds with:
     * - HTTP Status 400 BAD REQUEST
     */
    @ParameterizedTest
    @CsvSource({
            "+6141234567890123456789", // >20 length
            "1234_5678", //special char in number
            "0", //single digit zero
            "%^&*()$#@", //all special chars
            "123abc456def789" //numbers with chars
    })
    void activatePhoneNumber_invalidTooLong_shouldReturnBadRequest(String badPhoneNumber) throws Exception {
        mockMvc.perform(patch(ACTIVATE_PHONE_NUMBER_PATH, 1, badPhoneNumber))
                .andExpect(status().isBadRequest());
    }

    private MvcResult getMvcResult(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(request().asyncStarted())
                .andReturn();
    }


}
