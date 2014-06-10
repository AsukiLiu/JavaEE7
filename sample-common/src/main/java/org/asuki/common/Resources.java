package org.asuki.common;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources {

    @Produces
    public Logger loggerProducer(InjectionPoint ip) {

        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass());
    }
}
