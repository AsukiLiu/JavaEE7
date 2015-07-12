package org.asuki.webservice.rs.resource;

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.asuki.dao.PostDao;
import org.asuki.model.entity.Post;

@Path("streaming")
@Stateless
public class StreamingResource {

    @Inject
    private PostDao postDao;

    @GET
    @Path("list-all")
    @PermitAll
    @Produces({ APPLICATION_JSON })
    @TransactionAttribute(NEVER)
    public Response listAllPosts() {

        return noCacheResponseBuilder(OK).entity(new StreamingOutput() {

            private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            private static final int RECORDS_PER_ROUND_TRIP = 3;

            @Override
            public void write(OutputStream stream) throws IOException,
                    WebApplicationException {

                int recordPosition = 0;
                int recordSize = postDao.countRecords();

                try (PrintWriter writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(stream)))) {

                    writer.print("{\"result\": [");

                    while (recordSize > 0) {
                        List<Post> posts = postDao.listPostsByPage(
                                recordPosition, RECORDS_PER_ROUND_TRIP);

                        for (Post post : posts) {
                            if (recordPosition > 0) {
                                writer.print(",");
                            }

                            writer.print(createPostJson(post));

                            recordPosition++;
                        }

                        recordSize -= RECORDS_PER_ROUND_TRIP;
                    }

                    writer.print("]}");
                }
            }

            private String createPostJson(Post post) {
                return Json
                        .createObjectBuilder()
                        .add("created_date",
                                DATE_FORMAT.format(post.getCreatedDate()))
                        .add("title", post.getTitle())
                        .add("body", post.getBody()).build().toString();
            }

        }).build();
    }

    private ResponseBuilder noCacheResponseBuilder(Status status) {
        // no caching
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }

}
