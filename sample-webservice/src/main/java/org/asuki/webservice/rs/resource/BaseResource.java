package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseResource {

    @Context
    private SecurityContext securityContext;

    @Context
    private Providers providers;

    protected String getPrincipal() {
        return securityContext.getUserPrincipal().getName();
    }

    protected StreamingOutput getJsonStream(Object object) {

        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {

                final ContextResolver<ObjectMapper> resolver = providers
                        .getContextResolver(ObjectMapper.class,
                                APPLICATION_JSON_TYPE);

                ObjectMapper mapper = resolver == null ? new ObjectMapper()
                        : resolver.getContext(ObjectMapper.class);

                mapper.writeValue(output, object);
            }
        };
    }
}
