package org.asuki.webservice.rs.interceptor;

import static java.lang.Thread.currentThread;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;

@Provider
public class CustomInterceptor implements ReaderInterceptor, WriterInterceptor {

    @Inject
    private Logger log;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context)
            throws IOException, WebApplicationException {

        String methodName = currentThread().getStackTrace()[1].getMethodName();

        Object entity = context.proceed();

        log.info(methodName + " " + entity.toString());

        return entity;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {

        String methodName = currentThread().getStackTrace()[1].getMethodName();

        Object entity = context.getEntity();

        log.info(methodName + " " + entity.toString());

        context.proceed();
    }
}
