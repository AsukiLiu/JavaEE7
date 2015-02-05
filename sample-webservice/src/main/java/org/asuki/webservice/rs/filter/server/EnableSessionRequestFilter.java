package org.asuki.webservice.rs.filter.server;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.filter.annotation.EnableSession;
import org.slf4j.Logger;

@Provider
@EnableSession
public class EnableSessionRequestFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        HttpSession existingSession = request.getSession(false);
        if (existingSession == null) {
            HttpSession newSession = request.getSession(true);
            newSession.setMaxInactiveInterval(-1);
            log.info("New Session Id: {}", newSession.getId());

            // Just for test
            newSession.setAttribute("principal", "admin");
        }
    }
}