package org.asuki.webservice.rs.resource;

import static java.lang.String.valueOf;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static org.asuki.webservice.rs.entity.PersonDatabase.getSecond;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.asuki.webservice.rs.annotation.PATCH;
import org.asuki.webservice.rs.entity.Person;
import org.asuki.webservice.rs.entity.PersonDatabase;

//http://localhost:8080/sample-web/rs/persons
@Path("persons")
@Produces(APPLICATION_JSON)
public class PersonResource extends BaseResource {

    private static final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    @GET
    public Collection<Person> getAll() {
        return persons.values();
    }

    @GET
    @Path("{id}")
    public Person getPerson(
            // @formatter:off
            @PathParam("id")
            @NotNull
            @Pattern(regexp = "[0-9]+")
            String id) {
            // @formatter:on

        return persons.get(id);
    }

    @POST
    @Path("create")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response createPerson(
            // @formatter:off
            @FormParam("id")
            @NotNull(message = "{person.id.notnull}")
            @Pattern(regexp = "[0-9]+", message = "{person.id.pattern}")
            String id,
            @FormParam("name")
            @Size(min = 2, max = 30, message = "{person.name.size}")
            String name) {
            // @formatter:on

        Person person = new Person();
        person.setId(Integer.valueOf(id));
        person.setName(name);

        persons.put(id, person);
        return status(CREATED).entity(person).build();
    }

    @PATCH
    @Path("dummy")
    @Produces(APPLICATION_JSON)
    public Response doSomething() {

        Person person = new Person();
        person.setId(101);
        person.setName("Andy");

        StreamingOutput output = getJsonStream(person);
        return ok().entity(output).build();
    }

    @GET
    @Path("/etag/{id}")
    public Response getPersonById(@PathParam("id") int id, @Context Request req) {

        CacheControl cache = new CacheControl();
        cache.setMaxAge(24 * 30 * 30);

        String etagValue = valueOf(getSecond(PersonDatabase
                .getLastModifiedById(id)));
        EntityTag etag = new EntityTag(etagValue);

        Response.ResponseBuilder response = req.evaluatePreconditions(etag);
        if (response != null) {
            // Prerequisite: add "If-None-Match" to request header
            // Return 304 HTTP status
            return response.cacheControl(cache).tag(etag).build();
        }

        return ok(PersonDatabase.getPersonById(id)).cacheControl(cache)
                .tag(etag).build();
    }

    @PUT
    @Path("/etag/{id}")
    public Response updatePersonById(@PathParam("id") int id) {
        PersonDatabase.updatePerson(id);
        return status(200).build();
    }

}
