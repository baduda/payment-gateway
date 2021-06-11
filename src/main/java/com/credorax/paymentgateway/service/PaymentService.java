package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.domain.Payment;
import com.credorax.paymentgateway.persistanse.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class PaymentService {
    @NonNull
    private final PaymentRepository paymentRepository;

    public void processPayment(@NonNull Payment payment) {
        paymentRepository.save(payment);
    }

    public Optional<Payment> retrievePayment(Long invoice) {
        return paymentRepository.findByInvoice(invoice);
    }
}
