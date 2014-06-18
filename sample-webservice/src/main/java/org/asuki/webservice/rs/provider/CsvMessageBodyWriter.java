package org.asuki.webservice.rs.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces("text/csv")
public class CsvMessageBodyWriter implements MessageBodyWriter<String[][]> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        return type == String[][].class;
    }

    @Override
    public long getSize(String[][] t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        return -1;
    }

    @Override
    public void writeTo(String[][] t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream outputStream) throws IOException,
            WebApplicationException {

        try (PrintWriter out = new PrintWriter(outputStream)) {
            for (String[] row : t) {
                for (String column : row) {
                    out.printf("%s,", column);
                }
                out.println();
            }
        }
    }
}
