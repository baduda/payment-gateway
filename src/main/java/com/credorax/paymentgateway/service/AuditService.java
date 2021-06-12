package com.credorax.paymentgateway.service;

import com.credorax.paymentgateway.dto.PaymentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    @NonNull private final ObjectMapper objectMapper;

    @Value("${com.credorax.paymentgateway.audit.folder:.}")
    String auditFolder;

    @Async
    public Future<Path> addRequest(PaymentDto payment) {
        try {
            var fileName = new SimpleDateFormat("'audit-'yyyyMMddHHmmssSSS'.json'").format(new Date());
            var auditFile = new File(auditFolder, fileName);
            objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(auditFile, payment);
            return AsyncResult.forValue(auditFile.toPath());
        }
        catch (IOException e) {
            log.error("Cannot create audit log", e);
        }
        return null;
    }
}
