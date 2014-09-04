package org.asuki.webservice.rs.exception;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.entity.JsonResponse;
import org.slf4j.Logger;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private Logger log;

    private static final Response RESPONSE;
    private static final JsonResponse JSON = new JsonResponse("FAILURE");

    static {
        RESPONSE = Response.status(INTERNAL_SERVER_ERROR).entity(JSON).build();
    }

    @Produces(APPLICATION_JSON)
    @Override
    public Response toResponse(Throwable ex) {

        log.info(ex.getClass().getName());
        ex.printStackTrace();

        JSON.setErrorMsg(ex.getMessage());

        return RESPONSE;
    }

}