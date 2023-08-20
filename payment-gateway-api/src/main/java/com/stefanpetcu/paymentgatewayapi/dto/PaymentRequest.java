package com.stefanpetcu.paymentgatewayapi.dto;

import com.stefanpetcu.paymentgatewayapi.util.validation.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

/**
 * Validated in {@link com.stefanpetcu.paymentgatewayapi.service.PaymentsService#actionPayment(PaymentRequest)}
 */
public record PaymentRequest(
        // TODO: generalise to add support for more source types
        @NotNull(message = "must be included in the request")
        @Valid
        PaymentSourceCard source,

        // We're limiting our maximum single transfer amount to a conservative £21,000,000 for our MVP.
        @NotNull(message = "must be included in the request")
        @Positive @Max(2_100_000_000)
        Long amount_in_minor,

        @NotNull(message = "must be included in the request")
        @ValueOfEnum(enumClass = Currency.class)
        String currency,

        @Pattern(regexp = "^[a-zA-Z0-9:,. ]+$",
                message = "may only contain alphanumeric characters " +
                        "and one or more of the characters: [colon (:), comma (,), dot (.), space( )]")
        @Length(max = 50)
        String reference
) {
}
