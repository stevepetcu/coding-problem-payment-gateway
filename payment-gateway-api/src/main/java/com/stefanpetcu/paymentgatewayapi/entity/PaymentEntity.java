package com.stefanpetcu.paymentgatewayapi.entity;

import com.stefanpetcu.paymentgatewayapi.dto.Currency;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

// TODO: validate.
@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "amount_in_minor", nullable = false)
    private Long amountInMinor;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "reference", nullable = false)
    private String reference;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_source_details_id", referencedColumnName = "id", nullable = false)
    private PaymentSourceCardEntity source;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public PaymentEntity(UUID id,
                         PaymentStatus status,
                         Long amountInMinor,
                         Currency currency,
                         String reference,
                         PaymentSourceCardEntity source,
                         OffsetDateTime createdAt,
                         OffsetDateTime updatedAt) {
        super();
        this.id = id;
        this.status = status;
        this.amountInMinor = amountInMinor;
        this.currency = currency;
        this.reference = reference;
        this.source = source;
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

    public PaymentEntity withStatus(PaymentStatus status) {
        return new PaymentEntity(
                this.id,
                status,
                this.amountInMinor,
                this.currency,
                this.reference,
                this.source,
                this.createdAt,
                this.updatedAt
        );
    }
}
