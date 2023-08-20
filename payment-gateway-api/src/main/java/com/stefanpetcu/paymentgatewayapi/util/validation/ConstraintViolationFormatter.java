package com.stefanpetcu.paymentgatewayapi.util.validation;

import com.stefanpetcu.paymentgatewayapi.dto.FormattedValidationErrorsHashMap;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class ConstraintViolationFormatter {
    public <T> FormattedValidationErrorsHashMap getFormattedValidationErrorsHashMapFrom(
            Set<ConstraintViolation<T>> violations) {
        var violationsHashMap = new HashMap<String, ArrayList<String>>();

        for (ConstraintViolation<T> violation : violations) {
            var violationPropertyPathString = violation.getPropertyPath().toString();
            if (!violationsHashMap.containsKey(violationPropertyPathString)) {
                violationsHashMap.put(violationPropertyPathString, new ArrayList<>(List.of(violation.getMessage())));
            } else {
                violationsHashMap.get(violationPropertyPathString).add(violation.getMessage());
            }
        }

        return new FormattedValidationErrorsHashMap(violationsHashMap);
    }
}
