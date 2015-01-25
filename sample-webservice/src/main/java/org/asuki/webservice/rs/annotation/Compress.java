package org.asuki.webservice.rs.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RUNTIME)
public @interface Compress {
}
