package com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto;

import com.stefanpetcu.paymentgatewayapi.dto.PaymentStatus;
import com.stefanpetcu.paymentgatewayapi.util.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;

public record SimBankPaymentOrderResponse(@NotNull @ValueOfEnum(enumClass = PaymentStatus.class) String status) {
}
