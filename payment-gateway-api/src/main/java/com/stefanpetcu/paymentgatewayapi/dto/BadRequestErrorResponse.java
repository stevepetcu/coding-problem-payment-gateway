package com.stefanpetcu.paymentgatewayapi.dto;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: validate.
public record BadRequestErrorResponse(String error, HashMap<String, ArrayList<String>> details) {
}
