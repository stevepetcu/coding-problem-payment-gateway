package com.stefanpetcu.paymentgatewayapi.util;

import com.stefanpetcu.paymentgatewayapi.data.PaymentSourceCardEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentSourceCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardFingerprintGeneratorTest {
    @Test
    void test_generateFingerprintFor_returnsExpectedResult_givenValidInputPaymentSourceCard() {
        var fingerprintGenerator = new CardFingerprintGenerator();

        var card = new PaymentSourceCard(
                "CARD",
                PaymentSourceCardEntityDataProvider.STUBBED_CARD_NUMBER,
                1,
                2025,
                "123"
        );

        var result = fingerprintGenerator.generateFingerprintFor(card);

        assertEquals(PaymentSourceCardEntityDataProvider.STUBBED_CARD_NUMBER + "-123", result);
    }
}
