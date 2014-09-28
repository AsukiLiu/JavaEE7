package org.asuki.webservice.rs.filter.server;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.data.DummyUserDatabase;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;
import org.slf4j.Logger;

@Provider
public class SecurityRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger log;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    // @formatter:off
    private static final ServerResponse RESPONSE_UNAUTHORIZED = new ServerResponse(
            UNAUTHORIZED.getReasonPhrase(), 
            UNAUTHORIZED.getStatusCode(), 
            new Headers<Object>());;
    private static final ServerResponse RESPONSE_FORBIDDEN = new ServerResponse(
            FORBIDDEN.getReasonPhrase(), 
            FORBIDDEN.getStatusCode(), 
            new Headers<Object>());;
    private static final ServerResponse RESPONSE_ERROR = new ServerResponse(
            INTERNAL_SERVER_ERROR.getReasonPhrase(), 
            INTERNAL_SERVER_ERROR.getStatusCode(), 
            new Headers<Object>());
    // @formatter:on

    @Override
    public void filter(ContainerRequestContext requestContext) {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext
                .getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }

        if (method.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(RESPONSE_FORBIDDEN);
            return;
        }

        List<String> authorization = requestContext.getHeaders().get(
                AUTHORIZATION_PROPERTY);
        if (authorization == null || authorization.isEmpty()) {
            requestContext.abortWith(RESPONSE_UNAUTHORIZED);
            return;
        }

        String encodedInfo = authorization.get(0).replaceFirst(
                AUTHENTICATION_SCHEME + " ", "");

        String usernameAndPassword = null;
        try {
            usernameAndPassword = new String(Base64.decode(encodedInfo));
        } catch (IOException e) {
            log.error(e.getMessage());
            requestContext.abortWith(RESPONSE_ERROR);
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword,
                ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        log.info("User name:{}, Password:{}", username, password);

        if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method
                    .getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<>(
                    asList(rolesAnnotation.value()));

            if (!isUserAllowed(username, password, rolesSet)) {
                requestContext.abortWith(RESPONSE_UNAUTHORIZED);
                return;
            }
        }
    }

    private boolean isUserAllowed(String username, String password,
            Set<String> rolesSet) {

        String userRole = DummyUserDatabase.getUserRole(username, password);
        return rolesSet.contains(userRole);
    }

}
