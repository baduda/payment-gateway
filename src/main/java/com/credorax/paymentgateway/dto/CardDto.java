package com.credorax.paymentgateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull
public class CardDto {
    @NotBlank
    @Size(min = 16, max = 16)
    @Digits(integer = 16, fraction = 0)
    private String pan;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMyy")
    private Date expiry;

    @Size(min = 3, max = 3)
    @Digits(integer = 3, fraction = 0)
    private String cvv;
}