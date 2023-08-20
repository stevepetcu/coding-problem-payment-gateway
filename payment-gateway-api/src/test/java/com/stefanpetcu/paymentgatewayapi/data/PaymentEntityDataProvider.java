package com.stefanpetcu.paymentgatewayapi.data;

import com.stefanpetcu.paymentgatewayapi.dto.Currency;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentStatus;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentEntity;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentSourceCardEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface PaymentEntityDataProvider {
    default PaymentEntity paymentEntity(PaymentSourceCardEntity paymentSourceCardEntity) {
        var now = OffsetDateTime.now();

        return new PaymentEntity(
                UUID.randomUUID(),
                PaymentStatus.PENDING,
                10000L,
                Currency.GBP,
                "Some payment reference.",
                paymentSourceCardEntity,
                now,
                now
        );
    }
}
