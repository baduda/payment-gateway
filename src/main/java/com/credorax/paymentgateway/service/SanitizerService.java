package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.dto.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public class SanitizerService {
    public PaymentDto cleanup(PaymentDto dto) {
        var sanitizedDto = dto.toBuilder().build();
        sanitizedDto.getCardholder().setName("*".repeat(dto.getCardholder().getName().length()));
        sanitizedDto.getCard().setExpiry("****");
        sanitizedDto.getCard().setPan("************" + dto.getCard().getPan().substring(12));
        sanitizedDto.getCard().setCvv(null);
        return sanitizedDto;
    }
}
