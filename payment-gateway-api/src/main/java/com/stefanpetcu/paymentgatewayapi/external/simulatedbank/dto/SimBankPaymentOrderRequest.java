package com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto;

// TODO: Validate.
public record SimBankPaymentOrderRequest(
        String card_number,
        Integer card_exp_month,
        Integer card_exp_year,
        String card_cvv,
        Long payment_amount_in_minor,
        String payment_currency,
        String reference
) {
}
