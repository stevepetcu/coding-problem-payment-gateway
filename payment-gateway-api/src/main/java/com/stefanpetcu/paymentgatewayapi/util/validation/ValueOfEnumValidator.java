package com.stefanpetcu.paymentgatewayapi.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> validValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        validValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || validValues.contains(value.toString())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        String defaultMessage = context.getDefaultConstraintMessageTemplate();
        String formattedMessage = String.format(defaultMessage, validValues.toString());

        context.buildConstraintViolationWithTemplate(formattedMessage).addConstraintViolation();

        return false;
    }
}
