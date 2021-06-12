package com.credorax.paymentgateway.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.with;
import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Value("${com.credorax.paymentgateway.audit.folder}")
    String auditFolder;

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

        var lastAudit = Files.list(Path.of(auditFolder))
                             .filter(path -> path.getFileName().toString().matches("audit-.*json"))
                             .map(Path::toFile)
                             .max(comparing(File::lastModified))
                             .map(File::toPath)
                             .orElseThrow();

        var auditContent = Files.readString(lastAudit);
        assertThat(auditContent).isEqualTo("{\n" +
                                           "  \"invoice\" : 1234567,\n" +
                                           "  \"amount\" : 1299,\n" +
                                           "  \"currency\" : \"EUR\",\n" +
                                           "  \"card\" : {\n" +
                                           "    \"pan\" : \"************4242\",\n" +
                                           "    \"expiry\" : \"****\"\n" +
                                           "  },\n" +
                                           "  \"cardholder\" : {\n" +
                                           "    \"name\" : \"**********\",\n" +
                                           "    \"email\" : \"email@domain.com\"\n" +
                                           "  }\n" +
                                           "}");
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
    public void whenRequestedGet_thenSanitizedPaymentIsRetrieved() throws IOException {
        with().param("invoice", "1234567")
              .when()
              .get("/payments")
              .then()
              .statusCode(HttpStatus.OK.value())
              .contentType(ContentType.JSON)
              .assertThat()
              .body("invoice", is(1234567))
              .body("cardholder.name", is("**********"))
              .body("card.pan", is("************4242"))
              .body("card.expiry", is("****"))
              .body("card.cvv", nullValue());
    }
}
