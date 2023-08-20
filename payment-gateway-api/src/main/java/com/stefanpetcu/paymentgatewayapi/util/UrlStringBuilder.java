package com.stefanpetcu.paymentgatewayapi.util;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Component
@Validated
public class UrlStringBuilder {
    @NotBlank
    @URL
    @Value("${service.self.domain}")
    private final String domain;

    /**
     * Get the URL for a resource on the domain registered for this service.
     */
    public String getUrlFor(List<@Pattern(regexp = "^[a-zA-Z0-9\\-]+$",
            message = "the path segments may only contain " +
                    "alphanumeric characters and dashes") @Valid String> pathSegments) {
        var strBuilder = new StringBuilder(domain);

        for (String segment : pathSegments) {
            strBuilder.append("/").append(segment);
        }

        return strBuilder.toString();
    }
}
