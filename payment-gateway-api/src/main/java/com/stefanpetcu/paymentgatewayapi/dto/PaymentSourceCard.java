package com.stefanpetcu.paymentgatewayapi.dto;

import com.stefanpetcu.paymentgatewayapi.util.validation.ValueOfEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

/**
 * Validated in {@link com.stefanpetcu.paymentgatewayapi.service.PaymentsService#actionPayment(PaymentRequest)}
 */
public record PaymentSourceCard(
        // TODO: payment sources should be generalised; also, the
        //  PaymentSourceCard class should only accept CARD as a type
        @NotNull(message = "must be included in the request")
        @ValueOfEnum(enumClass = PaymentSourceType.class)
        String type,

        @NotNull() @CreditCardNumber
        String number,

        // TODO: improve validation, min = current month + year; add a max based to the year.
        @NotNull(message = "must be included in the request")
        @Min(1) @Max(12)
        Integer expiry_month,

        @NotNull(message = "must be included in the request")
        @Min(2023) @Max(2100)
        Integer expiry_year,

        @NotBlank(message = "must be included in the request and cannot be blank")
        @Length(min = 3, max = 4)
        String cvv
) {
}
