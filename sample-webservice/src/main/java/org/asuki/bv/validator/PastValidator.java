package org.asuki.bv.validator;

import java.time.LocalDate;
import java.time.temporal.Temporal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.asuki.bv.annotation.Past;

public class PastValidator implements ConstraintValidator<Past, Temporal> {

    @Override
    public void initialize(Past constraintAnnotation) {
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return LocalDate.from(value).isBefore(LocalDate.now());
    }

}
