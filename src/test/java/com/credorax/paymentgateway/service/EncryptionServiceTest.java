package com.credorax.paymentgateway.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EncryptionServiceTest {
    @Autowired EncryptionService securityService;

    @Test
    public void whenDataIsEncrypted_thenDateCanBeDecrypted() {
        var original = UUID.randomUUID().toString();

        var encrypted = securityService.encrypt(original);
        assertThat(encrypted).isNotEqualTo(original);

        var decrypted = securityService.decrypt(encrypted);
        assertThat(decrypted).isEqualTo(original);
    }
}
