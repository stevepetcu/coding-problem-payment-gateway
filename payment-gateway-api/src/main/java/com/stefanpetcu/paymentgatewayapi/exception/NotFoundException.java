package com.stefanpetcu.paymentgatewayapi.exception;

import org.springframework.util.StringUtils;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName) {
        super(StringUtils.capitalize(entityName) + " not found.");
    }
}
