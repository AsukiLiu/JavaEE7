package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.asuki.webservice.rs.data.DummyUserDatabase;
import org.asuki.webservice.rs.entity.User;

@Path("users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserResource {

    @PermitAll
    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int id) {
        User user = DummyUserDatabase.getUserById(id);
        return ok(user).build();
    }

    @RolesAllowed("ADMIN")
    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") int id) {
        User user = DummyUserDatabase.updateUser(id);
        return ok(user).build();
    }
}