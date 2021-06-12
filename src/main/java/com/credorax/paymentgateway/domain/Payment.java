package com.credorax.paymentgateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(indexes = @Index(columnList = "invoice", unique = true))
public class Payment {
    @Id
    @GeneratedValue
    Long id;

    Long invoice;
    String currency;
    Long amount;

    String name;
    String email;

    String pan;
    String expiry;
}
