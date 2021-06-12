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
    @NonNull private final EncryptionService encryptionService;
    @NonNull private final PaymentRepository paymentRepository;

    public void processPayment(@NonNull Payment payment) {
        var encryptedPayment = payment.toBuilder()
                                      .name(encryptionService.encrypt(payment.getName()))
                                      .pan(encryptionService.encrypt(payment.getPan()))
                                      .expiry(encryptionService.encrypt(payment.getExpiry()))
                                      .build();
        paymentRepository.save(encryptedPayment);
    }

    public Optional<Payment> retrievePayment(Long invoice) {
        var encryptedPayment = paymentRepository.findByInvoice(invoice);
        return encryptedPayment.map(payment -> payment.toBuilder()
                                                      .name(encryptionService.decrypt(payment.getName()))
                                                      .pan(encryptionService.decrypt(payment.getPan()))
                                                      .expiry(encryptionService.decrypt(payment.getExpiry()))
                                                      .build());
    }
}
