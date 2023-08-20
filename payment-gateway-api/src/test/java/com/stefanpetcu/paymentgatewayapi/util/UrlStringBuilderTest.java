package com.stefanpetcu.paymentgatewayapi.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UrlStringBuilderTest {
    private final UrlStringBuilder urlStringBuilder = new UrlStringBuilder("https://my-domain.com");

    @Test
    void test_getUrlFor_returnsExpectedResult_givenInputContainsOnlyAlphanumericCharactersAndDashes() {
        var result = urlStringBuilder.getUrlFor(List.of("foo", "bar", "bla-bla"));

        assertThat(result).isEqualTo("https://my-domain.com/foo/bar/bla-bla");
    }

    @Test
    void test_getUrlFor_returnsDomain_givenEmptyInputList() {
        var result = urlStringBuilder.getUrlFor(List.of());

        assertThat(result).isEqualTo("https://my-domain.com");
    }

    // TODO: test validation.
}
