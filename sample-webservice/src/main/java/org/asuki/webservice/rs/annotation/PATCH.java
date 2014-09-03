package org.asuki.webservice.rs.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.ws.rs.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD })
@Retention(RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {
}
