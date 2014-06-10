package org.asuki.webservice.rs.provider;

import static java.lang.String.format;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.google.common.base.Joiner;

@Tracked
@Provider
public class TrackProvider implements ContainerRequestFilter,
        ContainerResponseFilter {

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        log(requestContext);
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {

        log(responseContext);
    }

    private void log(ContainerRequestContext requestContext) {

        SecurityContext securityContext = requestContext.getSecurityContext();
        String authentication = securityContext.getAuthenticationScheme();
        Principal userPrincipal = securityContext.getUserPrincipal();

        // @formatter:off
        String securityInfo = (authentication != null || userPrincipal != null) 
                ? format(" Authentication: %s Principal: %s ", authentication,userPrincipal) 
                : " ";
        // @formatter:on

        UriInfo uriInfo = requestContext.getUriInfo();
        String method = requestContext.getMethod();
        List<Object> matchedResources = uriInfo.getMatchedResources();

        List<String> transformed = matchedResources.stream()
                .map(o -> o.getClass().getName()).collect(Collectors.toList());

        String resources = String.join(", ", transformed);

        log.info("{}[{}] {} -> {}", securityInfo, method, uriInfo.getPath(),
                resources);
    }

    private void log(ContainerResponseContext responseContext) {

        MultivaluedMap<String, String> stringHeaders = responseContext
                .getStringHeaders();

        StringBuilder headers = new StringBuilder();
        Joiner.on(',').withKeyValueSeparator(":")
                .appendTo(headers, stringHeaders);

        String headerInfo = "Response: " + headers.toString();

        Object entity = responseContext.getEntity();
        String entityInfo = entity != null ? entity.getClass().getName() : " ";

        log.info("{} <- {}", headerInfo, entityInfo);
    }
}
