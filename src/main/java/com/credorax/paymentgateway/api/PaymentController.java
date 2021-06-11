package com.credorax.paymentgateway.api;

import com.credorax.paymentgateway.dto.PaymentDto;
import com.credorax.paymentgateway.dto.PaymentResponseDto;
import com.fasterxml.jackson.databind.JsonMappingException;
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
@RequestMapping(value = "/v1")
public class PaymentController {

    @PostMapping(value = "/payments")
    public PaymentResponseDto submitPayment(@Valid @RequestBody PaymentDto payment) {
        return new PaymentResponseDto();
    }

    @GetMapping("/payments")
    public PaymentDto retrievePayment(@RequestParam Long invoice) {
        //todo: implement retrieve logic
        var dto = new PaymentDto();
        dto.setInvoice(1L);
        return dto;
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

        return new PaymentResponseDto(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public PaymentResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var field = ((JsonMappingException) ex.getCause()).getPath()
                                                          .stream()
                                                          .map(JsonMappingException.Reference::getFieldName)
                                                          .collect(joining("."));
        return new PaymentResponseDto(Map.of(field, "invalid format"));
    }
}
