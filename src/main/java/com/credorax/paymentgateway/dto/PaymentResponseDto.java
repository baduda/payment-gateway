package com.credorax.paymentgateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResponseDto {
    @NonNull boolean approved;
    @NonNull Map<String, String> errors;

    public static PaymentResponseDto success() {
        return new PaymentResponseDto(true, emptyMap());
    }

    public static PaymentResponseDto fail(Map<String, String> errors) {
        return new PaymentResponseDto(false, errors);
    }
}
