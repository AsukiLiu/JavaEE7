package org.asuki.common;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Resources {

    @Produces
    public Logger loggerProducer(InjectionPoint ip) {

        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(INDENT_OUTPUT);
    }
}
