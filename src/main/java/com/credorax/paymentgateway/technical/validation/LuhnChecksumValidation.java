package com.credorax.paymentgateway.technical.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LuhnChecksumValidation implements ConstraintValidator<LuhnChecksum, String> {

    public void initialize(LuhnChecksum constraint) { }

    public boolean isValid(String pan, ConstraintValidatorContext context) {
        int checkSum = 0;
        boolean isSecond = false;
        for (int i = pan.length() - 1; i >= 0; i--) {
            int digit = pan.charAt(i) - '0';
            if (isSecond) digit = digit * 2;
            checkSum += digit / 10;
            checkSum += digit % 10;
            isSecond = !isSecond;
        }
        return (checkSum % 10 == 0);
    }
}