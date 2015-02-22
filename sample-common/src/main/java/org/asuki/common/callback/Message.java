package org.asuki.common.callback;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.asuki.common.callback.MessageType.NORMAL;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Message {
    MessageType type() default NORMAL;
}
