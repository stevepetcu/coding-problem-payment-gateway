package com.stefanpetcu.paymentgatewayapi.exception;

public class ApiAuthException extends RuntimeException {
    public ApiAuthException(String message) {
        super(message);
    }
}
