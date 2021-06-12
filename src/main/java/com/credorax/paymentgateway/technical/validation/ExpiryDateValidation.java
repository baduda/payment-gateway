package com.credorax.paymentgateway.technical.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ExpiryDateValidation implements ConstraintValidator<ExpiryDate, String> {

    public void initialize(LuhnChecksum constraint) {
    }

    public boolean isValid(String expiryDate, ConstraintValidatorContext context) {
        try {
            var date = new SimpleDateFormat("MMyy").parse(expiryDate);
            return date.after(Date.from(Instant.now()));
        }
        catch (Exception e) {
            return false;
        }
    }
}