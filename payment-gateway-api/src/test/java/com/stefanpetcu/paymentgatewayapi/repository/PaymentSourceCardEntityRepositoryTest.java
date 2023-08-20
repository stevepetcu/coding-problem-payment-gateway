package com.stefanpetcu.paymentgatewayapi.repository;

import com.stefanpetcu.paymentgatewayapi.SpringTestContextConfiguration;
import com.stefanpetcu.paymentgatewayapi.data.PaymentSourceCardEntityDataProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentSourceCardEntityRepositoryTest extends SpringTestContextConfiguration
        implements PaymentSourceCardEntityDataProvider {
    @Test
    void test_savesSourceCardSuccessfully_givenValidSourceCard() {
        var sourceCardEntity = paymentSourceCardEntity();
        var savedSourceCardEntity = this.paymentSourceCardEntityRepository.save(sourceCardEntity);

        assertThat(sourceCardEntity.getId()).isEqualTo(savedSourceCardEntity.getId());
        assertThat(sourceCardEntity.getNumber()).isEqualTo(savedSourceCardEntity.getNumber());
        assertThat(sourceCardEntity.getExpiryMonth()).isEqualTo(savedSourceCardEntity.getExpiryMonth());
        assertThat(sourceCardEntity.getExpiryYear()).isEqualTo(savedSourceCardEntity.getExpiryYear());
        assertThat(sourceCardEntity.getCreatedAt()).isEqualTo(savedSourceCardEntity.getCreatedAt());
        assertThat(sourceCardEntity.getUpdatedAt()).isEqualTo(savedSourceCardEntity.getUpdatedAt());
    }
}
