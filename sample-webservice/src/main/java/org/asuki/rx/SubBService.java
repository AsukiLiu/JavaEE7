package org.asuki.rx;

import static javax.json.Json.createArrayBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.Singleton;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Singleton
@Path("sub-b")
public class SubBService {

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public JsonArray findById(@PathParam("id") String id) {

        // Dummy data
        return createArrayBuilder().add("Chinese").add("English")
                .add("Japanese").build();
    }

}