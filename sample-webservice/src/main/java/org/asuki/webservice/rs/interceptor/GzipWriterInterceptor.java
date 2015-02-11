package org.asuki.webservice.rs.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.asuki.webservice.rs.annotation.Compress;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Provider
@Compress
public class GzipWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {

        // Share context data
        ResteasyProviderFactory.pushContext(String.class, "some context data");

        MultivaluedMap<String, Object> headers = context.getHeaders();
        headers.add("Content-Encoding", "gzip");

        final OutputStream outputStream = context.getOutputStream();
        context.setOutputStream(new GZIPOutputStream(outputStream));
        context.proceed();
    }
}
