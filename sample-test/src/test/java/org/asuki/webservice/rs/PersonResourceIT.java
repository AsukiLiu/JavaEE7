package org.asuki.webservice.rs;

import static java.lang.Thread.currentThread;
import static java.util.Collections.emptyList;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.asuki.webservice.rs.entity.Person;
import org.asuki.webservice.rs.resource.PersonResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

//NOTE  VM arguments: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
@RunWith(Arquillian.class)
public class PersonResourceIT {

    private static final Logger LOG = Logger.getLogger(PersonResourceIT.class
            .getName());

    @ArquillianResource
    private URL baseURL;

    private Client client;
    private Response response;
    private String methodName;

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        final WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackage("org.asuki.webservice.rs")
                .addClasses(Person.class, PersonResource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"));

        LOG.info(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Before
    public void setup() {
        client = ClientBuilder.newBuilder().register(JacksonJsonProvider.class)
                .build();
    }

    @After
    public void tearDown() {
        logResponse(methodName, response);
    }

    @Test
    @RunAsClient
    @InSequence(10)
    public void shouldGetAllPersons() {
        methodName = currentThread().getStackTrace()[1].getMethodName();

        response = client.target(baseURL + "rs/persons")
                .request(APPLICATION_JSON).get();
        response.bufferEntity();

        assertEquals(emptyList(),
                response.readEntity(new GenericType<Collection<Person>>() {
                }));
    }

    @Test
    @RunAsClient
    @InSequence(20)
    public void shouldNotGetPerson() {
        methodName = currentThread().getStackTrace()[1].getMethodName();

        // @formatter:off
        response = client.target(baseURL + "rs/persons/{id}")
                .resolveTemplate("id", "test")
                .request(APPLICATION_JSON)
                .header("Accept-Language", "en")
                .get();
        // @formatter:on
        response.bufferEntity();

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    @RunAsClient
    @InSequence(30)
    public void shouldGetEmptyPerson() {
        methodName = currentThread().getStackTrace()[1].getMethodName();

        response = client.target(baseURL + "rs/persons/{id}")
                .resolveTemplate("id", "3").request(APPLICATION_JSON).get();
        response.bufferEntity();

        assertEquals(null, response.readEntity(Person.class));
    }

    @Test
    @RunAsClient
    @InSequence(40)
    public void shouldNotCreatePerson() {
        methodName = currentThread().getStackTrace()[1].getMethodName();

        Form form = new Form();

        response = client.target(baseURL + "rs/persons/create")
                .request(APPLICATION_JSON)
                .post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        response.bufferEntity();

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    @RunAsClient
    @InSequence(50)
    public void shouldCreatePerson() {
        methodName = currentThread().getStackTrace()[1].getMethodName();

        Person person = new Person();
        person.setId(101);
        person.setName("andy");

        Form form = new Form();
        form.param("id", String.valueOf(person.getId()));
        form.param("name", person.getName());

        response = client.target(baseURL + "rs/persons/create")
                .request(APPLICATION_JSON)
                .post(Entity.entity(form, APPLICATION_FORM_URLENCODED));
        response.bufferEntity();

        assertEquals(person, response.readEntity(Person.class));
    }

    private void logResponse(String method, Response response) {
        StringBuilder sb = new StringBuilder(method).append("\n");
        sb.append("Entity: ").append(response.readEntity(String.class))
                .append("\n");
        LOG.info(sb.toString());
    }
}
