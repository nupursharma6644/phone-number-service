package com.telco.phonenumberservice.blackboxtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PhoneNumberControllerE2ETest {

    @LocalServerPort
    private int port;

    private static String GET_ALL_PHONE_NUMBERS_PATH = "/v{version}/phoneNumbers";
    private static String GET_ALL_PHONE_NUMBERS_BY_CUSTOMER_PATH = "/v{version}/customers/{customerId}/phone-numbers";
    private static String ACTIVATE_PHONE_NUMBER_PATH = "/v{version}/phone-numbers/{phoneNumber}/activate";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getAllPhoneNumbers_validRequest_returnsListOfNumbers() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get(GET_ALL_PHONE_NUMBERS_PATH, "v1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()))
                .body("size()", greaterThan(0));
    }

    @Test
    void getAllPhoneNumbersByCustomer_validRequest_returnSuccess() {
        String testCustomerId = "cust123";

        given()
                .accept(ContentType.JSON)
                .when()
                .get(GET_ALL_PHONE_NUMBERS_BY_CUSTOMER_PATH, "v1",testCustomerId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2))
                .body("[0]", greaterThan(""));
    }

    @Test
    void activatePhoneNumber_validRequest_returnSuccess() {
        String testPhoneNumber = "1234567890";

        given()
                .accept(ContentType.JSON)
                .when()
                .patch(ACTIVATE_PHONE_NUMBER_PATH, "v1", testPhoneNumber)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("phoneNumber", equalTo(testPhoneNumber))
                .body("active", equalTo(true));
    }

}
