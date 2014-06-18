package org.asuki.webservice.rs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(APPLICATION_JSON)
public class ListMessageBodyWriter implements
        MessageBodyWriter<List<Map<String, String>>> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        // return List.class == type;
        return List.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(List<Map<String, String>> t, Class<?> type,
            Type genericType, Annotation[] annotations, MediaType mediaType) {

        return -1;
    }

    @Override
    public void writeTo(List<Map<String, String>> t, Class<?> type,
            Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream outputStream) throws IOException,
            WebApplicationException {

        try (PrintWriter out = new PrintWriter(outputStream)) {
            out.print(mapper.writeValueAsString(t));
        }
    }

}
