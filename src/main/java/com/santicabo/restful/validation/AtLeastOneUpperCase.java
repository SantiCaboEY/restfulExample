package com.santicabo.restful.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneUppercaseValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneUpperCase {
    String message() default "Password Debe contener por lo menos una mayúscula";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
