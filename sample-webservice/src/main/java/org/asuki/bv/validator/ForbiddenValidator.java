package org.asuki.bv.validator;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.asuki.bv.ValidatorHelper;
import org.asuki.bv.annotation.ForbiddenValues;

public class ForbiddenValidator implements
        ConstraintValidator<ForbiddenValues, String> {

    private String[] forbiddenValues;

    // Bean validation 1.1 feature
    @Inject
    private ValidatorHelper validatorHelper;

    @Override
    public void initialize(ForbiddenValues forbiddenValues) {
        this.forbiddenValues = forbiddenValues.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null
                || validatorHelper.isAllowed(value, forbiddenValues);
    }
}
