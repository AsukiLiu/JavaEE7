package org.asuki.webservice.rs.provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.jackson.CustomCharacterEscapes;
import org.asuki.webservice.rs.jackson.CustomSimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(APPLICATION_JSON)
public class ObjectMapperContextResolver implements
        ContextResolver<ObjectMapper> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        log.info("ObjectMapperContextResolver");

        mapper = new ObjectMapper();
        mapper.registerModule(new CustomSimpleModule());
        mapper.getFactory().setCharacterEscapes(new CustomCharacterEscapes());
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        log.info("getContext");

        return mapper;
    }
}
