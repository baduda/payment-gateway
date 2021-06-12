package com.credorax.paymentgateway.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentControllerTest {
    @LocalServerPort
    int port;

    @Value("classpath:valid-payment-request.json")
    Resource validRequest;
    @Value("classpath:invalid-payment-request.json")
    Resource invalidRequest;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
        RestAssured.basePath = "/v1";
    }

    @Test
    @Order(1)
    public void whenRequestedValidPost_thenPaymentIsCreated() throws IOException {
        with().contentType(ContentType.JSON)
              .body(validRequest.getInputStream())
              .when()
              .post("/payments")
              .then()
              .statusCode(HttpStatus.OK.value())
              .contentType(ContentType.JSON)
              .assertThat()
              .body("approved", equalTo(true))
              .body("errors", nullValue());
    }

    @Test
    @Order(1)
    public void whenRequestedInvalidPost_thenReturnedErrorsList() throws IOException {
        with().contentType(ContentType.JSON)
              .body(invalidRequest.getInputStream())
              .when()
              .post("/payments")
              .then()
              .statusCode(HttpStatus.BAD_REQUEST.value())
              .contentType(ContentType.JSON)
              .assertThat()
              .body("approved", equalTo(false))
              .body("errors.size()", is(8));
    }

    @Test
    @Order(2)
    public void whenRequestedGet_thenSanetizedPaymentIsRetrived() throws IOException {
        with().param("invoice", "1234567")
              .when()
              .get("/payments")
              .then()
              .statusCode(HttpStatus.OK.value())
              .contentType(ContentType.JSON);
    }
}
