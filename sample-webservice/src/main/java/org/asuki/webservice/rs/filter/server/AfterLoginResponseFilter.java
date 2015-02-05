package org.asuki.webservice.rs.filter.server;

import static javax.ws.rs.core.Response.Status.OK;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.filter.annotation.AfterLogin;
import org.slf4j.Logger;

@AfterLogin
@Provider
public class AfterLoginResponseFilter implements ContainerResponseFilter {

    @Context
    private HttpServletRequest request;

    @Inject
    private Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {

        if (responseContext.getStatus() == OK.getStatusCode()) {

            HttpSession session = request.getSession();
            log.info("Session Id: {}", session.getId());

            Object entity = responseContext.getEntity();
            log.info("Response Entity: {}", entity);
        }
    }

}