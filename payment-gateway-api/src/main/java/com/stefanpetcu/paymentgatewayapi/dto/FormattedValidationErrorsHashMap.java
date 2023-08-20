package com.stefanpetcu.paymentgatewayapi.dto;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: validate.
public record FormattedValidationErrorsHashMap(HashMap<String, ArrayList<String>> errors) {
}
