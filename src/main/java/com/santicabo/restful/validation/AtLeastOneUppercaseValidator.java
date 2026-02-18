package com.santicabo.restful.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneUppercaseValidator  implements ConstraintValidator<AtLeastOneUpperCase, String> {
    @Override
    public void initialize(AtLeastOneUpperCase constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.isBlank()) return false;
        return s.chars().anyMatch(Character::isUpperCase);
    }
}
