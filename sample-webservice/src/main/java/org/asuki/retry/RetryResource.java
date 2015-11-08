package org.asuki.retry;

import javax.annotation.security.PermitAll;
import javax.ejb.ConcurrentAccessTimeoutException;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("retry")
public class RetryResource {

    @EJB
    private TargetService service;

    @PermitAll
    @GET
    @RetryPolicy
    public String get() {
        String result;

        try {
            result = service.get();
        } catch (ConcurrentAccessTimeoutException e) {
            throw new WebApplicationException(Response.Status.REQUEST_TIMEOUT);
        }

        return result;
    }
}
