package com.stefanpetcu.paymentgatewayapi.controller;

import com.stefanpetcu.paymentgatewayapi.dto.PaymentRequest;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentResponse;
import com.stefanpetcu.paymentgatewayapi.exception.ApiAuthException;
import com.stefanpetcu.paymentgatewayapi.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PaymentsController {
    private static final String PAYMENTS_PATH = "payments";
    private final PaymentsService paymentsService;

    @PostMapping(PAYMENTS_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse postPayment(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false, defaultValue = "") String authHeader,
            @RequestBody PaymentRequest request
    ) {
        if (authHeader.isBlank()) {
            // TODO: implement auth filter.
            throw new ApiAuthException("An 'Authorization' header with any String value is required.");
        }

        var payment = paymentsService.actionPayment(request);

        return PaymentResponse.from(payment, paymentsService.getPaymentLinksFor(payment, PAYMENTS_PATH));
    }

    @GetMapping(PAYMENTS_PATH + "/{paymentId}")
    public PaymentResponse getPayment(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false, defaultValue = "") String authHeader,
            @PathVariable UUID paymentId
    ) {
        if (authHeader.isBlank()) {
            // TODO: implement auth filter.
            throw new ApiAuthException("An 'Authorization' header with any String value is required.");
        }

        var payment = paymentsService.findPaymentBy(paymentId);

        return PaymentResponse.from(
                payment,
                paymentsService.getPaymentLinksFor(payment, PAYMENTS_PATH));
    }
}
