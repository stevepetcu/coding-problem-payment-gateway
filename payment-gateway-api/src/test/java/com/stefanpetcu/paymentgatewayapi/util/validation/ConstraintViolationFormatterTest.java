package com.stefanpetcu.paymentgatewayapi.util.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstraintViolationFormatterTest {
    private Validator validator;
    private final ConstraintViolationFormatter constraintViolationFormatter = new ConstraintViolationFormatter();

    private record Foo(@NotBlank @Length(min = 5, max = 10) String myProp) {
    }

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    @Test
    void test_getFormattedValidationErrorsHashMapFrom_returnsFormattedValidationErrorsHashMap_givenErrorsArePresent() {
        var errors = validator.validate(new Foo("   "));
        var formattedErrors = constraintViolationFormatter.getFormattedValidationErrorsHashMapFrom(errors);

        assertEquals(formattedErrors.errors().size(), 1);
        assertTrue(formattedErrors.errors().containsKey("myProp"));
        assertTrue(formattedErrors.errors().get("myProp").contains("must not be blank"));
        assertTrue(formattedErrors.errors().get("myProp").contains("length must be between 5 and 10"));
    }

    @Test
    void test_getFormattedValidationErrorsHashMapFrom_returnsEmptyFormattedValidationErrorsHashMap_givenErrorsNotPresent() {
        var errors = validator.validate(new Foo("fooBar "));
        var formattedErrors = constraintViolationFormatter.getFormattedValidationErrorsHashMapFrom(errors);

        assertEquals(formattedErrors.errors().size(), 0);
    }
}
