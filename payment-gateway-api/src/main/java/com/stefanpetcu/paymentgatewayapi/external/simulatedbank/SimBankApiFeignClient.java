package com.stefanpetcu.paymentgatewayapi.external.simulatedbank;

import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderRequest;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "sim-bank", url = "${service.external.sim-bank.base-url}")
public interface SimBankApiFeignClient {
    // TODO: configure retry behaviour based on bank route/method/response code;
    //  implement an exponential backoff with a random jitter.

    @PostMapping(value = "${service.external.sim-bank.create-payment-order-path}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    SimBankPaymentOrderResponse initiatePayment(@RequestHeader("SB-Idempotency-Id") String idempotencyId,
                                                @Valid @RequestBody SimBankPaymentOrderRequest request);
}
