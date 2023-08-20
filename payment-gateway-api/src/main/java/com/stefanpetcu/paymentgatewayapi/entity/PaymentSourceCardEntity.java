package com.stefanpetcu.paymentgatewayapi.entity;

import com.stefanpetcu.paymentgatewayapi.dto.PaymentSourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * We're not saving the CVV; this is always required from the payer, as a security measure.
 */
// TODO: validate.
@Entity
@Table(name = "card_source_details")
@Getter
@NoArgsConstructor
public class PaymentSourceCardEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "expiry_month", nullable = false)
    private Integer expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private Integer expiryYear;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public PaymentSourceCardEntity(
            String id,
            String number,
            Integer expiryMonth,
            Integer expiryYear,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        super();
        this.id = id;
        this.number = number;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        var now = OffsetDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public PaymentSourceType getType() {
        return PaymentSourceType.CARD;
    }
}
