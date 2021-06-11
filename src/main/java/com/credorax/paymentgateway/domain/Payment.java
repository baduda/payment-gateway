package com.credorax.paymentgateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Currency;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "invoice", unique = true))
public class Payment {
    @Id
    @GeneratedValue
    Long id;

    Long invoice;
    Currency currency;
    Long amount;

    String name;
    String email;

    String pan;
    String lastFourDigits;
    Date expiry;
}
