package com.stefanpetcu.paymentgatewayapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanpetcu.paymentgatewayapi.SpringTestContextConfiguration;
import com.stefanpetcu.paymentgatewayapi.data.PaymentEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.data.PaymentSourceCardEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentsControllerTest extends SpringTestContextConfiguration
        implements PaymentSourceCardEntityDataProvider, PaymentEntityDataProvider {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_postPayment_returnsPaymentResponse_givenNoErrors() throws JsonProcessingException, JSONException {
        // Mock requests made to SimBank
        stubFor(post(urlPathEqualTo("/payment-orders"))
                .willReturn(aResponse()
                        .withBody("""
                                {
                                    "status": "SETTLED"
                                }"""
                        )
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var paymentRequestJson = """
                {
                    "source": {
                        "type": "CARD",
                        "number": "374245455400126",
                        "expiry_month": 12,
                        "expiry_year": 2025,
                        "cvv": "1234"
                    },
                    "amount_in_minor": 10000,
                    "currency": "GBP",
                    "reference": "My payment reference."
                }""";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer blabla");

        var response = testRestTemplate.postForEntity(
                "/payments",
                new HttpEntity<>(paymentRequestJson, headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        PaymentResponse paymentResponse = objectMapper.readValue(response.getBody(), PaymentResponse.class);

        var expectedResponseJson = """
                {
                    "id": "%s",
                    "status": "SETTLED",
                    "source": {
                        "fingerprint": "374245455400126-1234",
                        "type": "CARD",
                        "number": "***********0126",
                        "expiry_month": 12,
                        "expiry_year": 2025
                    },
                    "amount_in_minor": 10000,
                    "currency": "GBP",
                    "reference": "My payment reference.",
                    "_links": {
                        "self": "http://localhost:8080/payments/%s"
                    }
                }""".formatted(paymentResponse.id(), paymentResponse.id());

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }

    @Test
    void test_postPayment_returnsBadRequestErrorResponse_givenBadRequest() throws JSONException {
        var paymentRequestJson = """
                {
                    "source": {
                        "type": "CARD",
                        "number": "374245455400126",
                        "expiry_month": 12,
                        "expiry_year": 2025,
                        "cvv": "1234"
                    },
                    "amount_in_minor": -10000,
                    "currency": "GBP",
                    "reference": "My payment reference."
                }""";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer blabla");

        var response = testRestTemplate.postForEntity(
                "/payments",
                new HttpEntity<>(paymentRequestJson, headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        var expectedResponseJson = """
                {
                    "error": "Bad Request",
                    "details": {
                        "amount_in_minor": [
                            "must be greater than 0"
                        ]
                    }
                }""";

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }

    @Test
    void test_postPayment_returnsUnauthorisedResponse_givenAuthHeaderIsAbsent() throws JSONException {
        var paymentRequestJson = """
                {
                    "source": {
                        "type": "CARD",
                        "number": "374245455400126",
                        "expiry_month": 12,
                        "expiry_year": 2025,
                        "cvv": "1234"
                    },
                    "amount_in_minor": 10000,
                    "currency": "GBP",
                    "reference": "My payment reference."
                }""";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        var response = testRestTemplate.postForEntity(
                "/payments",
                new HttpEntity<>(paymentRequestJson, headers),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        var expectedResponseJson = """
                {
                    "message": "An 'Authorization' header with any String value is required."
                }""";

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }

    @Test
    void test_getPayment_returnsPaymentResponse_givenPaymentExists() throws JSONException {
        var paymentSourceCardEntity = paymentSourceCardEntity();
        var paymentEntity = paymentEntity(paymentSourceCardEntity);

        paymentSourceCardEntityRepository.save(paymentSourceCardEntity);
        paymentEntityRepository.save(paymentEntity);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer blabla");

        var response = testRestTemplate.exchange(
                "/payments/" + paymentEntity.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var expectedResponseJson = """
                {
                    "id": "%s",
                    "status": "PENDING",
                    "source": {
                        "fingerprint": "%s",
                        "type": "CARD",
                        "number": "***********0126",
                        "expiry_month": 1,
                        "expiry_year": 2025
                    },
                    "amount_in_minor": 10000,
                    "currency": "GBP",
                    "reference": "Some payment reference.",
                    "_links": {
                        "self": "http://localhost:8080/payments/%s"
                    }
                }""".formatted(
                paymentEntity.getId(),
                paymentEntity.getSource().getId(),
                paymentEntity.getId());

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }

    @Test
    void test_getPayment_returnsNotFoundResponse_givenPaymentIsAbsent() throws JSONException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer blabla");

        var response = testRestTemplate.exchange(
                "/payments/12345678-1234-1234-1234-123456789101",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        var expectedResponseJson = """
                {
                    "message": "Payment not found."
                }""";

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }

    @Test
    void test_getPayment_returnsUnauthorisedResponse_givenAuthHeaderIsAbsent() throws JSONException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        var response = testRestTemplate.exchange(
                "/payments/12345678-1234-1234-1234-123456789101",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        var expectedResponseJson = """
                {
                    "message": "An 'Authorization' header with any String value is required."
                }""";

        JSONAssert.assertEquals(expectedResponseJson, response.getBody(), true);
    }
}
