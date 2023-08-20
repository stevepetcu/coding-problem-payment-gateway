package com.stefanpetcu.paymentgatewayapi.exception;

import com.stefanpetcu.paymentgatewayapi.dto.BadRequestErrorResponse;
import com.stefanpetcu.paymentgatewayapi.dto.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiRequestValidationException.class)
    public BadRequestErrorResponse handleValidationException(ApiRequestValidationException exception) {
        // TODO: implement proper logging; where relevant, log one or more of: exception details, request trace id etc.
        System.out.println(exception.getErrors());
        return new BadRequestErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getErrors());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public GenericErrorResponse handleNotFoundException(NotFoundException exception) {
        return new GenericErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ApiAuthException.class)
    public GenericErrorResponse handleUnauthorisedException(ApiAuthException exception) {
        return new GenericErrorResponse(exception.getMessage());
    }
}
