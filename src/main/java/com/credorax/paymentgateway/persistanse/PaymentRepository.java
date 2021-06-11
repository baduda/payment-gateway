package com.credorax.paymentgateway.persistanse;


import com.credorax.paymentgateway.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    Optional<Payment> findByInvoice(Long invoice);
}
