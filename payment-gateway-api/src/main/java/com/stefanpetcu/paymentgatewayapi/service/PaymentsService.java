package com.stefanpetcu.paymentgatewayapi.service;

import com.stefanpetcu.paymentgatewayapi.dto.*;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentEntity;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentSourceCardEntity;
import com.stefanpetcu.paymentgatewayapi.exception.ApiRequestValidationException;
import com.stefanpetcu.paymentgatewayapi.exception.NotFoundException;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.SimBankApiGateway;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderResponse;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentEntityRepository;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentSourceCardEntityRepository;
import com.stefanpetcu.paymentgatewayapi.util.CardFingerprintGenerator;
import com.stefanpetcu.paymentgatewayapi.util.UrlStringBuilder;
import com.stefanpetcu.paymentgatewayapi.util.validation.ConstraintViolationFormatter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentsService {
    private final UrlStringBuilder urlStringBuilder;
    private final CardFingerprintGenerator cardFingerprintGenerator;

    private final Validator validator;
    private final ConstraintViolationFormatter constraintViolationFormatter;

    private final PaymentEntityRepository paymentEntityRepository;
    private final PaymentSourceCardEntityRepository paymentSourceCardEntityRepository;

    private final SimBankApiGateway simBankApiGateway;

    private FormattedValidationErrorsHashMap validatePaymentRequest(PaymentRequest request) {
        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        return constraintViolationFormatter.getFormattedValidationErrorsHashMapFrom(violations);
    }

    private FormattedValidationErrorsHashMap validatePaymentOrderResponse(SimBankPaymentOrderResponse response) {
        Set<ConstraintViolation<SimBankPaymentOrderResponse>> violations = validator.validate(response);

        return constraintViolationFormatter.getFormattedValidationErrorsHashMapFrom(violations);
    }

    private PaymentEntity stagePaymentIntent(PaymentRequest request) {
        var now = OffsetDateTime.now();

        var paymentSourceCardFingerprint = this.cardFingerprintGenerator.generateFingerprintFor(request.source());
        var paymentSourceCard = paymentSourceCardEntityRepository.findById(
                paymentSourceCardFingerprint
        ).orElseGet(() -> this.paymentSourceCardEntityRepository.save(new PaymentSourceCardEntity(
                paymentSourceCardFingerprint,
                request.source().number(),
                request.source().expiry_month(),
                request.source().expiry_year(),
                now,
                now
        )));

        return this.paymentEntityRepository.save(
                new PaymentEntity(
                        UUID.randomUUID(),
                        PaymentStatus.PENDING,
                        request.amount_in_minor(),
                        Currency.valueOf(request.currency()),
                        request.reference(),
                        paymentSourceCard,
                        now,
                        now
                )
        );
    }

    public PaymentEntity actionPayment(PaymentRequest request) {
        FormattedValidationErrorsHashMap requestValidationResult = this.validatePaymentRequest(request);

        if (!requestValidationResult.errors().isEmpty()) {
            throw new ApiRequestValidationException(requestValidationResult);
        }

        var payment = this.stagePaymentIntent(request);

        var paymentOrderResponse =
                simBankApiGateway.initiatePayment(payment, request.source().cvv());
        FormattedValidationErrorsHashMap paymentOrderResponseValidationResult =
                this.validatePaymentOrderResponse(paymentOrderResponse);

        // TODO:
        //  1. Overly simplified handling; we should log/alert + trigger some manual action to figure out
        //  what happened and remediate the situation.
        //  2. We should also handle other kinds of errors, such as timeouts, 4**, 5** etc.
        if (!paymentOrderResponseValidationResult.errors().isEmpty()) {
            return this.paymentEntityRepository.save(
                    payment.withStatus(PaymentStatus.FAILED)
            );
        }

        return this.paymentEntityRepository.save(
                payment.withStatus(PaymentStatus.valueOf(paymentOrderResponse.status()))
        );
    }

    public PaymentEntity findPaymentBy(UUID id) {
        return paymentEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("payment"));
    }

    public PaymentLinks getPaymentLinksFor(PaymentEntity payment, String paymentsPath) {
        return new PaymentLinks(urlStringBuilder.getUrlFor(
                List.of(paymentsPath, payment.getId().toString())));
    }
}
