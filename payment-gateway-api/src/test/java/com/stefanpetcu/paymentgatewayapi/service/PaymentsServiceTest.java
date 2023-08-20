package com.stefanpetcu.paymentgatewayapi.service;

import com.stefanpetcu.paymentgatewayapi.data.PaymentEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.data.PaymentSourceCardEntityDataProvider;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentLinks;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentRequest;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentSourceCard;
import com.stefanpetcu.paymentgatewayapi.dto.PaymentStatus;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentEntity;
import com.stefanpetcu.paymentgatewayapi.entity.PaymentSourceCardEntity;
import com.stefanpetcu.paymentgatewayapi.exception.ApiRequestValidationException;
import com.stefanpetcu.paymentgatewayapi.exception.NotFoundException;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.SimBankApiGateway;
import com.stefanpetcu.paymentgatewayapi.external.simulatedbank.dto.SimBankPaymentOrderResponse;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentEntityRepository;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentSourceCardEntityRepository;
import com.stefanpetcu.paymentgatewayapi.util.CardFingerprintGenerator;
import com.stefanpetcu.paymentgatewayapi.util.UrlStringBuilder;
import com.stefanpetcu.paymentgatewayapi.util.validation.ConstraintViolationFormatter;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentsServiceTest implements PaymentSourceCardEntityDataProvider, PaymentEntityDataProvider {
    private static final String STUBBED_CVV = "123";
    private final PaymentEntityRepository paymentEntityRepository =
            mock(PaymentEntityRepository.class);
    private final PaymentSourceCardEntityRepository paymentSourceCardEntityRepository =
            mock(PaymentSourceCardEntityRepository.class);

    private final SimBankApiGateway simBankApiGateway =
            mock(SimBankApiGateway.class);

    private PaymentsService service;

    @BeforeEach
    void setUp() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        service = new PaymentsService(
                new UrlStringBuilder("https://my-domain.com"),
                new CardFingerprintGenerator(),
                validator,
                new ConstraintViolationFormatter(),
                paymentEntityRepository,
                paymentSourceCardEntityRepository,
                simBankApiGateway
        );
    }

    @Test
    void test_actionPayment_createsAndReturnsPayment_givenSourceCardExistsAndNoErrorsOccur() {
        var paymentSourceCardEntity = paymentSourceCardEntity();
        var paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);

        // Mock having an existing PaymentSourceCardEntity
        when(paymentSourceCardEntityRepository
                .findById(paymentSourceCardEntity.getNumber() + "-" + STUBBED_CVV))
                .thenReturn(Optional.of(paymentSourceCardEntity));

        // Mock saving the PaymentEntity and return the same argument that the method was called with + capture it
        when(paymentEntityRepository.save(paymentEntityArgumentCaptor.capture()))
                .thenAnswer(payment -> payment.getArgument(0, PaymentEntity.class));

        when(simBankApiGateway.initiatePayment(any(PaymentEntity.class), eq(STUBBED_CVV)))
                .thenReturn(new SimBankPaymentOrderResponse("SETTLED"));

        var result = service.actionPayment(
                new PaymentRequest(
                        new PaymentSourceCard(
                                "CARD",
                                paymentSourceCardEntity.getNumber(),
                                paymentSourceCardEntity.getExpiryMonth(),
                                paymentSourceCardEntity.getExpiryYear(),
                                STUBBED_CVV
                        ),
                        10000L,
                        "GBP",
                        "My payment reference."
                )
        );

        var capturedPaymentEntityRepositoryCalls = paymentEntityArgumentCaptor.getAllValues();

        assertEquals(PaymentStatus.PENDING, capturedPaymentEntityRepositoryCalls.get(0).getStatus());
        assertEquals(PaymentStatus.SETTLED, capturedPaymentEntityRepositoryCalls.get(1).getStatus());

        assertEquals(capturedPaymentEntityRepositoryCalls.get(0).getId(), result.getId());
        assertEquals(PaymentStatus.SETTLED, result.getStatus());
    }

    @Test
    void test_actionPayment_createsAndReturnsPaymentAndPaymentSourceCard_givenNewSourceCardAndNoErrorsOccur() {
        var paymentSourceCardEntity = paymentSourceCardEntity();
        var paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);

        // Mock having an existing PaymentSourceCardEntity
        when(paymentSourceCardEntityRepository
                .findById(paymentSourceCardEntity.getNumber() + "-" + STUBBED_CVV))
                .thenReturn(Optional.empty());
        when(paymentSourceCardEntityRepository
                .save(any(PaymentSourceCardEntity.class)))
                .thenAnswer(paymentSourceCard -> paymentSourceCard.getArgument(0, PaymentSourceCardEntity.class));

        // Mock saving the PaymentEntity and return the same argument that the method was called with + capture it
        when(paymentEntityRepository.save(paymentEntityArgumentCaptor.capture()))
                .thenAnswer(payment -> payment.getArgument(0, PaymentEntity.class));

        when(simBankApiGateway.initiatePayment(any(PaymentEntity.class), eq(STUBBED_CVV)))
                .thenReturn(new SimBankPaymentOrderResponse("SETTLED"));

        var result = service.actionPayment(
                new PaymentRequest(
                        new PaymentSourceCard(
                                "CARD",
                                paymentSourceCardEntity.getNumber(),
                                paymentSourceCardEntity.getExpiryMonth(),
                                paymentSourceCardEntity.getExpiryYear(),
                                STUBBED_CVV
                        ),
                        10000L,
                        "GBP",
                        "My payment reference."
                )
        );

        var capturedPaymentEntityRepositoryCalls = paymentEntityArgumentCaptor.getAllValues();

        assertEquals(PaymentStatus.PENDING, capturedPaymentEntityRepositoryCalls.get(0).getStatus());
        assertEquals(PaymentStatus.SETTLED, capturedPaymentEntityRepositoryCalls.get(1).getStatus());

        assertEquals(capturedPaymentEntityRepositoryCalls.get(0).getId(), result.getId());
        assertEquals(PaymentStatus.SETTLED, result.getStatus());

        assertEquals(paymentSourceCardEntity.getNumber(), result.getSource().getNumber());
    }

    @Test
    void test_actionPayment_throwsApiRequestValidationException_givenBadRequest() {
        // TODO: exhaustively test the PaymentRequest validation separately.
        var exception = assertThrows(ApiRequestValidationException.class,
                () -> service.actionPayment(
                        new PaymentRequest(
                                new PaymentSourceCard(
                                        "CARDz",
                                        "12345",
                                        24,
                                        2000,
                                        "  "
                                ),
                                10000L,
                                "GBP",
                                "My payment reference."
                        )
                ));

        assertTrue(exception.getErrors().containsKey("source.expiry_month"));
        assertTrue(exception.getErrors().get("source.expiry_month").contains("must be less than or equal to 12"));

        assertTrue(exception.getErrors().containsKey("source.expiry_year"));
        assertTrue(exception.getErrors().get("source.expiry_year").contains("must be greater than or equal to 2023"));

        assertTrue(exception.getErrors().containsKey("source.number"));
        assertTrue(exception.getErrors().get("source.number").contains("invalid credit card number"));

        assertTrue(exception.getErrors().containsKey("source.type"));
        assertTrue(exception.getErrors().get("source.type").contains("must be one of: [CARD]"));

        assertTrue(exception.getErrors().containsKey("source.cvv"));
        assertThat(exception.getErrors().get("source.cvv"),
                containsInAnyOrder("must be included in the request and cannot be blank",
                        "length must be between 3 and 4"));
    }

    @Test
    void test_actionPayment_returnsPaymentWithStatusFailed_givenBadResponseFromTheBank() {
        var paymentSourceCardEntity = paymentSourceCardEntity();
        var paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);

        // Mock having an existing PaymentSourceCardEntity
        when(paymentSourceCardEntityRepository
                .findById(paymentSourceCardEntity.getNumber() + "-" + STUBBED_CVV))
                .thenReturn(Optional.of(paymentSourceCardEntity));

        // Mock saving the PaymentEntity and return the same argument that the method was called with + capture it
        when(paymentEntityRepository.save(paymentEntityArgumentCaptor.capture()))
                .thenAnswer(payment -> payment.getArgument(0, PaymentEntity.class));

        when(simBankApiGateway.initiatePayment(any(PaymentEntity.class), eq(STUBBED_CVV)))
                .thenReturn(new SimBankPaymentOrderResponse("UNKNOWN_STATUS"));

        var result = service.actionPayment(
                new PaymentRequest(
                        new PaymentSourceCard(
                                "CARD",
                                paymentSourceCardEntity.getNumber(),
                                paymentSourceCardEntity.getExpiryMonth(),
                                paymentSourceCardEntity.getExpiryYear(),
                                STUBBED_CVV
                        ),
                        10000L,
                        "GBP",
                        "My payment reference."
                )
        );

        var capturedPaymentEntityRepositoryCalls = paymentEntityArgumentCaptor.getAllValues();

        assertEquals(PaymentStatus.PENDING, capturedPaymentEntityRepositoryCalls.get(0).getStatus());
        assertEquals(PaymentStatus.FAILED, capturedPaymentEntityRepositoryCalls.get(1).getStatus());

        assertEquals(capturedPaymentEntityRepositoryCalls.get(0).getId(), result.getId());
        assertEquals(PaymentStatus.FAILED, result.getStatus());
    }

    @Test
    void test_findPaymentBy_returnsThePaymentEntity_givenAPaymentEntityWithTheProvidedIdExists() {
        var paymentEntity = paymentEntity(paymentSourceCardEntity());

        when(paymentEntityRepository.findById(paymentEntity.getId()))
                .thenReturn(Optional.of(paymentEntity));

        var result = service.findPaymentBy(paymentEntity.getId());

        assertEquals(paymentEntity.getClass(), result.getClass());
        assertEquals(paymentEntity.getId(), result.getId());
    }

    @Test
    void test_findPaymentBy_throwsNotFoundException_givenAPaymentEntityWithTheProvidedIdIsAbsent() {
        var paymentEntity = paymentEntity(paymentSourceCardEntity());

        when(paymentEntityRepository.findById(paymentEntity.getId()))
                .thenReturn(Optional.empty());

        var exception = assertThrows(NotFoundException.class,
                () -> service.findPaymentBy(paymentEntity.getId()));

        assertEquals("Payment not found.", exception.getMessage());
    }

    @Test
    void test_getPaymentLinksFor_returnsExpectedPaymentLinksObject_givenValidInputArguments() {
        var paymentEntity = paymentEntity(paymentSourceCardEntity());

        var result = service.getPaymentLinksFor(paymentEntity, "my-payments-path");

        assertEquals(PaymentLinks.class, result.getClass());
        assertEquals(
                "https://my-domain.com/my-payments-path/" + paymentEntity.getId().toString(),
                result.self()
        );
    }
}
