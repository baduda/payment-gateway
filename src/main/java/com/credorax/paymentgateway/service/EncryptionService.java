package com.credorax.paymentgateway.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EncryptionService {
    public String encrypt(String value) {
        return new String(Base64.getEncoder().encode(value.getBytes()));
    }

    public String decrypt(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
