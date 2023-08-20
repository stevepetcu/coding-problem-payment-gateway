package com.stefanpetcu.paymentgatewayapi.util;

import com.stefanpetcu.paymentgatewayapi.dto.PaymentSourceCard;
import org.springframework.stereotype.Component;

@Component
public class CardFingerprintGenerator {
    public String generateFingerprintFor(PaymentSourceCard card) {
        // TODO: this is a quick mock fingerprint; normally we'd generate a cryptographically secure fingerprint
        //  based on the card details + something like the client id, to avoid the ability to cross-reference
        //  cards between merchants if that is a requirement.
        //  Plus, we shouldn't be saving the CVV.
        //  Anyway, this will enable us to uniquely identify cards across multiple payments, for the
        //  purposes of this exercise.
        return card.number() + "-" + card.cvv();
    }
}
