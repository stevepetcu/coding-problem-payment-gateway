package com.stefanpetcu.paymentgatewayapi.data;

import com.stefanpetcu.paymentgatewayapi.entity.PaymentSourceCardEntity;

import java.time.OffsetDateTime;

public interface PaymentSourceCardEntityDataProvider {
    String STUBBED_CARD_FINGERPRINT = "FINGERPRINT12345677890QWERTY";
    String STUBBED_CARD_NUMBER = "374245455400126";

    default PaymentSourceCardEntity paymentSourceCardEntity() {
        var now = OffsetDateTime.now();
        return new PaymentSourceCardEntity(
                STUBBED_CARD_FINGERPRINT,
                STUBBED_CARD_NUMBER,
                1,
                2025,
                now,
                now
        );
    }
}
