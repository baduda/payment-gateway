package com.credorax.paymentgateway.technical.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LuhnChecksumValidationTest {
    LuhnChecksumValidation luhnChecksumValidation = new LuhnChecksumValidation();

    @Test
    public void whenPanIsValid_thenLuchValidationIsPassed() throws IOException {
        assertThat(luhnChecksumValidation.isValid("12345674", null)).isTrue();
    }

    @Test
    public void whenPanIsInvalid_thenLuchValidationIsFailed() throws IOException {
        assertThat(luhnChecksumValidation.isValid("13245674", null)).isFalse();
    }
}
