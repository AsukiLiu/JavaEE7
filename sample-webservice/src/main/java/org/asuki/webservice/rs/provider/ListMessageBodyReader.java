package org.asuki.webservice.rs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Consumes(APPLICATION_JSON)
public class ListMessageBodyReader implements
        MessageBodyReader<List<Map<String, String>>> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        // return List.class == type;
        return List.class.isAssignableFrom(type);
    }

    @Override
    public List<Map<String, String>> readFrom(
            Class<List<Map<String, String>>> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream inputStream)
            throws IOException, WebApplicationException {

        return mapper.readValue(inputStream,
                new TypeReference<List<Map<String, String>>>() {
                });
    }

}
