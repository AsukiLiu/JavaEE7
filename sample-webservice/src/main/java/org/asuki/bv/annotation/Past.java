package org.asuki.bv.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.asuki.bv.validator.PastValidator;

@Constraint(validatedBy = PastValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Past {

    String message() default "{org.asuki.bv.annotation.Past.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
