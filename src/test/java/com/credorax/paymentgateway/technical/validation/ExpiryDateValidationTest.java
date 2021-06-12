package com.credorax.paymentgateway.technical.validation;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiryDateValidationTest {
    ExpiryDateValidation expiryDateValidation = new ExpiryDateValidation();

    @Test
    public void whenExpiryDateInPast_thenValidationIsFailed() throws IOException {
        assertThat(expiryDateValidation.isValid("0101", null)).isFalse();
    }

    @Test
    public void whenExpiryDateInFuture_thenValidationIsPassed() throws IOException {
        assertThat(expiryDateValidation.isValid("1240", null)).isTrue();
    }
}
