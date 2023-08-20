package com.stefanpetcu.paymentgatewayapi.dto;

import jakarta.validation.constraints.NotBlank;

// TODO: validate.
public record PaymentLinks(@NotBlank String self) {

}
