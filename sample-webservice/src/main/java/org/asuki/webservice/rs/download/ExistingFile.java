package org.asuki.webservice.rs.download;

import static org.apache.commons.io.FileUtils.ONE_MB;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;

public class ExistingFile {

    private static final Logger log = LoggerFactory
            .getLogger(ExistingFile.class);

    private final String method;
    private final FilePointer filePointer;

    public ExistingFile(String method, FilePointer filePointer) {
        this.method = method;
        this.filePointer = filePointer;
    }

    public Response redirect(Optional<String> requestEtagOpt,
            Optional<Date> ifModifiedSinceOpt) {
        return serveWithCaching(requestEtagOpt, ifModifiedSinceOpt,
                this::redirectDownload);
    }

    public Response handle(Optional<String> requestEtagOpt,
            Optional<Date> ifModifiedSinceOpt) {
        return serveWithCaching(requestEtagOpt, ifModifiedSinceOpt,
                this::handleDownload);
    }

    private Response serveWithCaching(Optional<String> requestEtagOpt,
            Optional<Date> ifModifiedSinceOpt,
            Function<FilePointer, Response> notCachedResponse) {

        return cached(requestEtagOpt, ifModifiedSinceOpt) ? notModified(filePointer)
                : notCachedResponse.apply(filePointer);
    }

    private boolean cached(Optional<String> requestEtagOpt,
            Optional<Date> ifModifiedSinceOpt) {
        boolean matchingEtag = requestEtagOpt.map(filePointer::matchesEtag)
                .orElse(false);
        boolean notModifiedSince = ifModifiedSinceOpt.map(Date::toInstant)
                .map(filePointer::modifiedAfter).orElse(false);
        return matchingEtag || notModifiedSince;
    }

    private Response redirectDownload(FilePointer filePointer) {
        log.info("Redirecting {} '{}'", method, filePointer);

        try {
            return Response
                    .status(Status.MOVED_PERMANENTLY)
                    .location(
                            new URI("/download/handle/"
                                    + filePointer.getOriginalName()))
                    .entity(null).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Response handleDownload(FilePointer filePointer) {
        log.info("Handling {} '{}'", method, filePointer);

        InputStream resource = method.equals(HttpMethod.GET) ? buildResource(filePointer)
                : null;
        return response(filePointer, Status.OK, resource);
    }

    private InputStream buildResource(FilePointer filePointer) {
        InputStream inputStream = filePointer.open();
        RateLimiter throttler = RateLimiter.create(ONE_MB); // ONE_KB
        return new ThrottlingInputStream(inputStream, throttler);
    }

    private Response notModified(FilePointer filePointer) {
        return response(filePointer, Status.NOT_MODIFIED, null);
    }

    private Response response(FilePointer filePointer, Status status,
            Object body) {

        final ResponseBuilder responseBuilder = Response.status(status)
                .tag(filePointer.getEtag())
                .header("Content-Length ", filePointer.getSize())
                .lastModified(Date.from(filePointer.getLastModified()));

        filePointer.getMediaType().map(this::toMediaType)
                .ifPresent(responseBuilder::type);

        return responseBuilder.entity(body).build();
    }

    private MediaType toMediaType(com.google.common.net.MediaType input) {
        return input
                .charset()
                .transform(
                        c -> new MediaType(input.type(), input.subtype(), c
                                .toString()))
                .or(new MediaType(input.type(), input.subtype()));
    }

}
