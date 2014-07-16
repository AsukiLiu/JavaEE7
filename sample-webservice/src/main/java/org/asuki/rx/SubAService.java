package org.asuki.rx;

import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.Singleton;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Singleton
@Path("sub-a")
public class SubAService {

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public JsonObject findById(@PathParam("id") String id) {

        // Dummy data
        // @formatter:off
        return createObjectBuilder()
            .add("author", "Andy")
            .add("city", "Tokyo")
            .build();
        // @formatter:on
    }

}
