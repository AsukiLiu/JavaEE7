package org.asuki.bv.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.asuki.bv.annotation.NameDifferent;

@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
public class NameValidator implements
        ConstraintValidator<NameDifferent, Object[]> {

    @Override
    public void initialize(NameDifferent constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object[] parameters,
            ConstraintValidatorContext context) {
        return parameters == null
                || (parameters.length == 2 && !parameters[0]
                        .equals(parameters[1]));
    }
}
