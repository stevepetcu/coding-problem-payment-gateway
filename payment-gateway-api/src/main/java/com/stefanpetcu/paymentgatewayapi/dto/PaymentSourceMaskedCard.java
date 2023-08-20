package com.stefanpetcu.paymentgatewayapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

// TODO: validate.
@Validated
public record PaymentSourceMaskedCard(
        @NotBlank String fingerprint,
        @NotNull PaymentSourceType type,
        // TODO: improve validation, make sure that the card number is indeed masked.
        @NotNull @Length(min = 8, max = 19) String number,
        // TODO: improve validation, min current month, year as specified in the PaymentSourceCard DTO.
        @NotNull @Min(1) @Max(12) Integer expiry_month,
        @NotNull @Min(2023) Integer expiry_year
) {
    public PaymentSourceMaskedCard {
        // Mask the card number:
        number = StringUtils.overlay(
                number,
                StringUtils.repeat("*", number.length() - 4), 0, number.length() - 4
        );
    }
}
