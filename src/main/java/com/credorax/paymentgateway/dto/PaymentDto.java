package com.credorax.paymentgateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@JsonInclude(Include.NON_NULL)
@NotNull
public class PaymentDto {
    @Positive
    private Long invoice;

    @Positive
    private Long amount;

    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    @Valid
    private CardDto card;

    @NotNull
    @Valid
    private CardholderDto cardholder;

}
