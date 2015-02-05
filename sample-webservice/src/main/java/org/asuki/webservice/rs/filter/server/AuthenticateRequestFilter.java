package org.asuki.webservice.rs.filter.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.filter.annotation.Authenticate;
import org.slf4j.Logger;

@Provider
@Authenticate
public class AuthenticateRequestFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            forbidden(requestContext);
            return;
        }

        Object attribute = session.getAttribute("principal");
        if (attribute == null) {
            forbidden(requestContext);
            return;
        }

        String principal = attribute.toString();
        String sessionId = session.getId();

        log.info("Sesssion Id: {}, Principal: {}", sessionId, principal);

        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public boolean isUserInRole(String role) {
                return "admin".equals(role) ? true : false;
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public Principal getUserPrincipal() {
                return new Principal() {

                    @Override
                    public String getName() {
                        return principal;
                    }
                };
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });

    }

    private void forbidden(ContainerRequestContext requestContext) {
        MediaType mediaType = requestContext.getMediaType();

        Map<String, String> json = new HashMap<>();

        if (mediaType != null && mediaType.toString().equals(APPLICATION_JSON)) {
            json.put("msg", "Authentication failed");
        }

        requestContext.abortWith(status(FORBIDDEN).entity(json).build());
    }
}