package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;

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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.asuki.webservice.rs.annotation.PATCH;
import org.asuki.webservice.rs.entity.Person;

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
        return Response.status(CREATED).entity(person).build();
    }

    @PATCH
    @Path("dummy")
    @Produces(APPLICATION_JSON)
    public Response doSomething() {

        Person person = new Person();
        person.setId(101);
        person.setName("Andy");

        StreamingOutput output = getJsonStream(person);
        return Response.ok().entity(output).build();
    }
}
