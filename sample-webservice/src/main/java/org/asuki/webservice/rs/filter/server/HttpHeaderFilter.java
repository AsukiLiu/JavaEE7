package org.asuki.webservice.rs.filter.server;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

@Provider
@PreMatching
public class HttpHeaderFilter implements ContainerRequestFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        log.info("ContainerRequestFilter");

        String override = requestContext.getHeaders().getFirst(
                "X-HTTP-Method-Override");

        if (override != null) {
            requestContext.setMethod(override);
        }
    }
}
