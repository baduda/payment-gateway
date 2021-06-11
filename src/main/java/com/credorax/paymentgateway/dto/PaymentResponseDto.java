package com.credorax.paymentgateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class PaymentResponseDto {
    @NonNull private final boolean approved;
    @NonNull private final Map<String, String> errors;

    public PaymentResponseDto() {
        this.approved = true;
        this.errors = Collections.emptyMap();
    }

    public PaymentResponseDto(Map<String, String> errors) {
        this.approved = false;
        this.errors = errors;
    }

    public @NonNull boolean isApproved() {
        return this.approved;
    }

    public @NonNull Map<String, String> getErrors() {
        return this.errors;
    }
}
