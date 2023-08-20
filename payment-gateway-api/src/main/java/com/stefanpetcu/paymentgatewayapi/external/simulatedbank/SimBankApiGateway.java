package com.stefanpetcu.paymentgatewayapi.external.simulatedbank;

import com.stefanpetcu.paymentgatewayapi.entity.PaymentEntity;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderRequest;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptor layer for the SimBank.
 * Normally, this would be a separate API layer that handles the adaptors for all
 * the various banks we can connect to – but having an adaptor layer between the
 * various APIs is good nonetheless, to limit the extent of changes needed those
 * APIs change.
 */
@RequiredArgsConstructor
@Component
public class SimBankApiGateway {
    private final SimBankApiFeignClient simBankApiFeignClient;

    public SimBankPaymentOrderResponse initiatePayment(PaymentEntity payment, String cvv) {
        var request = new SimBankPaymentOrderRequest(
                payment.getSource().getNumber(),
                payment.getSource().getExpiryMonth(),
                payment.getSource().getExpiryYear(),
                cvv,
                payment.getAmountInMinor(),
                payment.getCurrency().name(),
                payment.getReference()
        );

        // TODO: better idempotency header, should request one from the merchant and maybe pass it along here.
        return this.simBankApiFeignClient.initiatePayment(payment.getId().toString(), request);
    }
}
