package org.asuki.bv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { ForbiddenValidator.class })
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ForbiddenValues {
    String[] value() default { "password" };

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{org.asuki.bv.ForbiddenValues.message}";
}
