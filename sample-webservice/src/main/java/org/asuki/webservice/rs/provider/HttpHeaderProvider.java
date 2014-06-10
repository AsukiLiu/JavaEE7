package org.asuki.webservice.rs.provider;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class HttpHeaderProvider implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        String override = requestContext.getHeaders().getFirst(
                "X-HTTP-Method-Override");

        if (override != null) {
            requestContext.setMethod(override);
        }
    }
}
