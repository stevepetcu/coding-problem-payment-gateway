package com.stefanpetcu.paymentgatewayapi.repository;

import com.stefanpetcu.paymentgatewayapi.SpringTestContextConfiguration;
import com.stefanpetcu.paymentgatewayapi.data.PaymentEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.data.PaymentSourceCardEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentEntityRepositoryTest extends SpringTestContextConfiguration
        implements PaymentSourceCardEntityDataProvider, PaymentEntityDataProvider {
    @Test
    void test_savesPaymentSuccessfully_givenValidPayment() {
        var sourceCardEntity = paymentSourceCardEntity();
        var savedSourceCardEntity = this.paymentSourceCardEntityRepository.save(sourceCardEntity);

        var paymentEntity = paymentEntity(savedSourceCardEntity);
        var savedPaymentEntity = this.paymentEntityRepository.save(paymentEntity);

        assertThat(paymentEntity.getId()).isEqualTo(savedPaymentEntity.getId());
        assertThat(paymentEntity.getStatus()).isEqualTo(savedPaymentEntity.getStatus());
        assertThat(paymentEntity.getAmountInMinor()).isEqualTo(savedPaymentEntity.getAmountInMinor());
        assertThat(paymentEntity.getCurrency()).isEqualTo(savedPaymentEntity.getCurrency());
        assertThat(paymentEntity.getReference()).isEqualTo(savedPaymentEntity.getReference());
        assertThat(paymentEntity.getCreatedAt()).isEqualTo(savedPaymentEntity.getCreatedAt());
        assertThat(paymentEntity.getUpdatedAt()).isEqualTo(savedPaymentEntity.getUpdatedAt());
        assertThat(savedPaymentEntity.getSource()).isNotNull();
        assertThat(savedPaymentEntity.getSource().getId()).isEqualTo(sourceCardEntity.getId());
    }

    @Test
    void test_updatesThePaymentEntityStatusAndItsUpdatedAtDateOnly_givenValidPaymentStatusChange() {
        var sourceCardEntity = paymentSourceCardEntity();
        var savedSourceCardEntity = this.paymentSourceCardEntityRepository.save(sourceCardEntity);

        var paymentEntity = paymentEntity(savedSourceCardEntity);
        var savedPaymentEntity = this.paymentEntityRepository.save(paymentEntity);
        var updatedPaymentEntity = this.paymentEntityRepository.save(
                savedPaymentEntity.withStatus(PaymentStatus.SETTLED)
        );

        assertThat(savedPaymentEntity.getId()).isEqualTo(updatedPaymentEntity.getId());
        assertThat(savedPaymentEntity.getCreatedAt()).isEqualTo(updatedPaymentEntity.getCreatedAt());
        assertThat(updatedPaymentEntity.getStatus()).isEqualTo(PaymentStatus.SETTLED);
        assertThat(updatedPaymentEntity.getUpdatedAt()).isAfter(savedPaymentEntity.getUpdatedAt());
    }

    @Test
    void test_doesNotUpdateThePaymentSourceUpdatedAt_givenThePaymentEntityUpdates() {
        var sourceCardEntity = paymentSourceCardEntity();
        var savedSourceCardEntity = this.paymentSourceCardEntityRepository.save(sourceCardEntity);

        var paymentEntity = paymentEntity(savedSourceCardEntity);
        var savedPaymentEntity = this.paymentEntityRepository.save(paymentEntity);
        var updatedPaymentEntity = this.paymentEntityRepository.save(
                savedPaymentEntity.withStatus(PaymentStatus.SETTLED)
        );

        assertThat(sourceCardEntity.getUpdatedAt())
                .isEqualTo(updatedPaymentEntity.getSource().getUpdatedAt());
    }
}
