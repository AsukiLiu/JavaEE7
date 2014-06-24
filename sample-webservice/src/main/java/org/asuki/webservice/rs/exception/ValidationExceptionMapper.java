package org.asuki.webservice.rs.exception;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements
        ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {

        return buildResponse(unwrapException(exception), TEXT_PLAIN,
                INTERNAL_SERVER_ERROR);
    }

    private Response buildResponse(Object entity, String mediaType,
            Status status) {

        ResponseBuilder builder = Response.status(status).entity(entity);
        builder.type(mediaType);
        return builder.build();
    }

    private String unwrapException(Throwable t) {
        StringBuffer sb = new StringBuffer();
        recurseException(sb, t);
        return sb.toString();
    }

    private void recurseException(StringBuffer sb, Throwable t) {
        if (t == null) {
            return;
        }

        sb.append(t.toString());
        if (t.getCause() != null && t != t.getCause()) {
            sb.append('[');
            recurseException(sb, t.getCause());
            sb.append(']');
        }
    }

}
