package com.credorax.paymentgateway.dto;

import com.credorax.paymentgateway.technical.validation.ExpiryDate;
import com.credorax.paymentgateway.technical.validation.LuhnChecksum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull
public class CardDto {
    @NotBlank
    @Size(min = 16, max = 16)
    @Digits(integer = 16, fraction = 0)
    @LuhnChecksum
    private String pan;

    @ExpiryDate
    @Size(min = 4, max = 4)
    @Digits(integer = 4, fraction = 0)
    private String expiry;

    @Size(min = 3, max = 3)
    @Digits(integer = 3, fraction = 0)
    private String cvv;
}