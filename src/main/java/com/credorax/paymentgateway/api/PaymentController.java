package com.credorax.paymentgateway.api;

import com.credorax.paymentgateway.domain.Payment;
import com.credorax.paymentgateway.dto.CardDto;
import com.credorax.paymentgateway.dto.CardholderDto;
import com.credorax.paymentgateway.dto.PaymentDto;
import com.credorax.paymentgateway.dto.PaymentResponseDto;
import com.credorax.paymentgateway.service.AuditService;
import com.credorax.paymentgateway.service.PaymentService;
import com.credorax.paymentgateway.service.SanitizerService;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toUnmodifiableMap;


@RestController
@RequestMapping("/v1")
@AllArgsConstructor
@NonNull
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final SanitizerService sanitizerService;
    private final AuditService auditService;

    @PostMapping(value = "/payments")
    public PaymentResponseDto submitPayment(@Valid @RequestBody PaymentDto payment) {
        paymentService.processPayment(Payment.builder()
                                             .invoice(payment.getInvoice())
                                             .currency(payment.getCurrency())
                                             .amount(payment.getAmount())
                                             .pan(payment.getCard().getPan())
                                             .expiry(payment.getCard().getExpiry())
                                             .email(payment.getCardholder().getEmail())
                                             .name(payment.getCardholder().getName())
                                             .build());
        var sanitizedDto = sanitizerService.cleanup(payment);
        auditService.addRequest(sanitizedDto);

        return PaymentResponseDto.success();
    }

    @GetMapping("/payments")
    public PaymentDto retrievePayment(@RequestParam Long invoice) {
        return paymentService.retrievePayment(invoice)
                             .map(payment -> PaymentDto.builder()
                                                       .invoice(payment.getInvoice())
                                                       .amount(payment.getAmount())
                                                       .currency(payment.getCurrency())
                                                       .card(CardDto.of(payment.getPan(), payment.getExpiry(), null))
                                                       .cardholder(CardholderDto.of(payment.getName(), payment.getEmail()))
                                                       .build())
                             .map(sanitizerService::cleanup)
                             .orElseGet(PaymentDto::new);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public PaymentResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors()
                       .stream()
                       .map(FieldError.class::cast)
                       .collect(toUnmodifiableMap(
                               error -> error.getField(),
                               error -> requireNonNullElse(error.getDefaultMessage(), "invalid formal"),
                               (error1, error2) -> error1 + ", " + error2));

        return PaymentResponseDto.fail(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public PaymentResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var field = ((JsonMappingException) ex.getCause()).getPath()
                                                          .stream()
                                                          .map(JsonMappingException.Reference::getFieldName)
                                                          .collect(joining("."));
        return PaymentResponseDto.fail(Map.of(field, "invalid format"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public PaymentResponseDto handleException(Exception ex) {
        log.error("PaymentController error", ex);
        return PaymentResponseDto.fail(Map.of());
    }
}
