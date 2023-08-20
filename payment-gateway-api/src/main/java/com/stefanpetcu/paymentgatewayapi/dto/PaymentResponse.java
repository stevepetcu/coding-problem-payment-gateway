package com.stefanpetcu.paymentgatewayapi.dto;

import com.stefanpetcu.paymentgatewayapi.entity.PaymentEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

// TODO: validate.
public record PaymentResponse(
        @NotBlank String id,
        // TODO: generalise to add support for more source types

        @NotNull PaymentStatus status,
        @NotNull PaymentSourceMaskedCard source,
        @NotNull @Positive Long amount_in_minor,
        @NotNull Currency currency,
        @Pattern(regexp = "[a-zA-Z0-9:;,.\\-_]+") @Length(max = 50) String reference,
        @NotNull PaymentLinks _links
) {
    public static PaymentResponse from(PaymentEntity payment, PaymentLinks links) {
        return new PaymentResponse(
                payment.getId().toString(),
                payment.getStatus(),
                new PaymentSourceMaskedCard(
                        payment.getSource().getId(),
                        payment.getSource().getType(),
                        payment.getSource().getNumber(),
                        payment.getSource().getExpiryMonth(),
                        payment.getSource().getExpiryYear()
                ),
                payment.getAmountInMinor(),
                payment.getCurrency(),
                payment.getReference(),
                links
        );
    }
}
