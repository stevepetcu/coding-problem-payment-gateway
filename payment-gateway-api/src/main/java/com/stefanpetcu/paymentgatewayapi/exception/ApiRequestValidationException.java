package com.stefanpetcu.paymentgatewayapi.exception;

import com.stefanpetcu.paymentgatewayapi.dto.FormattedValidationErrorsHashMap;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiRequestValidationException extends RuntimeException {
    private final FormattedValidationErrorsHashMap validationResult;

    public ApiRequestValidationException(FormattedValidationErrorsHashMap validationResult) {
        super(validationResult.toString());

        this.validationResult = validationResult;
    }

    public HashMap<String, ArrayList<String>> getErrors() {
        return validationResult.errors();
    }

    @Override
    public String toString() {
        return validationResult.errors().toString();
    }
}
