package org.asuki.webservice.rs.resource;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public abstract class BaseResource {

    @Context
    private SecurityContext securityContext;

    protected String getPrincipal() {
        return securityContext.getUserPrincipal().getName();
    }
}
