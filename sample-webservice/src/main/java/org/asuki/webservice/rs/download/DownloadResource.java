package org.asuki.webservice.rs.download;

import static java.util.Optional.ofNullable;

import java.util.Date;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/download")
public class DownloadResource {

    @Inject
    private FileStorage storage;

    // http://localhost:8080/sample-web/rs/download/handle/test.zip
    @GET
    @Path("/handle/{filename}")
    @PermitAll
    public Response handle(@PathParam("filename") String filename,
            @Context Request req,
            @HeaderParam(HttpHeaders.IF_NONE_MATCH) String ifNoneMatch,
            @HeaderParam(HttpHeaders.IF_MODIFIED_SINCE) Date ifModifiedSince) {

        // @formatter:off
        return findExistingFile(req.getMethod(), filename)
                .map(file -> file.handle(ofNullable(ifNoneMatch), ofNullable(ifModifiedSince)))
                .orElse(Response.status(Status.NOT_FOUND).build());
                // .orElseGet(() -> Response.status(Status.NOT_FOUND).build());
        // @formatter:on
    }

    // http://localhost:8080/sample-web/rs/download/redirect/test.zip
    @GET
    @Path("/redirect/{filename}")
    @PermitAll
    public Response redirect(@PathParam("filename") String filename,
            @Context Request req,
            @HeaderParam(HttpHeaders.IF_NONE_MATCH) String ifNoneMatch,
            @HeaderParam(HttpHeaders.IF_MODIFIED_SINCE) Date ifModifiedSince) {

        // @formatter:off
        return findExistingFile(req.getMethod(), filename)
                 .map(file -> file.redirect(ofNullable(ifNoneMatch), ofNullable(ifModifiedSince)))
                .orElse(Response.status(Status.NOT_FOUND).build());
                // .orElseGet(() -> Response.status(Status.NOT_FOUND).build());
        // @formatter:on
    }

    private Optional<ExistingFile> findExistingFile(String method,
            String filename) {
        return storage.findFile(filename).map(
                pointer -> new ExistingFile(method, pointer));
    }

}
