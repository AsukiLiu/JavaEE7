package org.asuki.common.javase.annotation;

import java.lang.annotation.Repeatable;

@Repeatable(Hints.class)
public @interface Hint {
    String value() default "";
}
