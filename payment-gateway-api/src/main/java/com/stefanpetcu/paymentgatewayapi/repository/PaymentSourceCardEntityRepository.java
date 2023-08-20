package com.stefanpetcu.paymentgatewayapi.repository;

import com.stefanpetcu.paymentgatewayapi.entity.PaymentSourceCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSourceCardEntityRepository extends JpaRepository<PaymentSourceCardEntity, String> {
}
