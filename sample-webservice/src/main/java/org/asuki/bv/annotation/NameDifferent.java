package org.asuki.bv.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.asuki.bv.validator.NameValidator;

@Constraint(validatedBy = { NameValidator.class })
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface NameDifferent {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{org.asuki.bv.annotation.NameDifferent.message}";
}
