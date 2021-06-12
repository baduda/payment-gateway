package com.credorax.paymentgateway.technical.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExpiryDateValidation.class)
public @interface ExpiryDate {
    String message() default "is expired";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}