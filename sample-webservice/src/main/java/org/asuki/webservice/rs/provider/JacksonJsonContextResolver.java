package org.asuki.webservice.rs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(APPLICATION_JSON)
public class JacksonJsonContextResolver implements
        ContextResolver<ObjectMapper> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // Ignore all empty value
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return MAPPER;
    }
}