package org.asuki.webservice.rs;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.asuki.common.Resources;
import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.RoastType;
import org.asuki.webservice.rs.filter.client.CustomClientFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

import lombok.SneakyThrows;

//NOTE  VM arguments: -Djava.util.logging.manager=org.jboss.logmanager.LogManager
@RunWith(Arquillian.class)
@RunAsClient
public class DemoResourceIT {

    private static final String MEDIA_TYPE = APPLICATION_JSON;

    private static final Logger LOG = Logger.getLogger(DemoResourceIT.class
            .getName());

    private Client client;
    private WebTarget root;
    private boolean isCompleted;

    @ArquillianResource
    private URL baseURL;

    @Deployment
    public static WebArchive createDeployment() throws IOException {
        final WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, "org.asuki.webservice.rs")
                .addClasses(Resources.class)
                .addAsWebInfResource("META-INF/jboss-deployment-structure.xml",
                        "jboss-deployment-structure.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"));

        LOG.info(war.toString(Formatters.VERBOSE));

        return war;
    }

    @Before
    public void setup() {
        client = ClientBuilder.newClient().register(
                new CustomClientFilter("andy", "1234"));
        root = client.target(baseURL + "rs/demo");
    }

    @SneakyThrows
    @Test
    public void testCrud() {

        final Entity<Bean> entity = createEntity();
        final Bean bean = entity.getEntity();

        // @POST
        Response response = root.request(MEDIA_TYPE).post(entity,
                Response.class);
        assertThat(response.getStatus(), is(201));

        response.close();

        // @GET
        Bean result = root.path(bean.getName()).request(MEDIA_TYPE)
                .get(Bean.class);
        assertThat(result, is(bean));

        result = root.path("query").queryParam("id", bean.getName())
                .request(MEDIA_TYPE).get(Bean.class);
        assertThat(result, is(bean));

        Future<Bean> future = root.path("query")
                .queryParam("id", bean.getName()).request(MEDIA_TYPE).async()
                .get(Bean.class);

        result = future.get(3, TimeUnit.SECONDS);
        assertThat(result, is(bean));

        // @GET
        Collection<Bean> allBeans = root.request(MEDIA_TYPE).get(
                new GenericType<Collection<Bean>>() {
                });

        assertThat(allBeans.size(), is(1));
        assertThat(allBeans, hasItem(bean));

        // @DELETE
        response = root.path(bean.getName()).request(MEDIA_TYPE)
                .delete(Response.class);
        assertThat(response.getStatus(), is(204));

        // @GET
        response = root.path(bean.getName()).request(MEDIA_TYPE)
                .get(Response.class);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void testAsync() {

        final Entity<Bean> entity = createEntity();

        assertThat(isCompleted, is(false));

        root.path("async").path("X001").request(MEDIA_TYPE).async()
                .post(entity, new InvocationCallback<Bean>() {
                    @Override
                    public void completed(Bean bean) {
                        isCompleted = true;
                    }

                    @Override
                    public void failed(Throwable thrwbl) {
                    }
                });

        assertThat(isCompleted, is(false));
    }

    @SneakyThrows
    @Test
    public void testFuture() {
        final String id = "X001";

        final Entity<Bean> entity = createEntity();

        Future<Response> future = root.path("async").path(id)
                .request(MEDIA_TYPE).async().post(entity);
        Response response = future.get(3, TimeUnit.SECONDS);
        Bean bean = response.readEntity(Bean.class);

        assertThat(bean.getType(), is(RoastType.LIGHT));
        assertThat(bean.getName(), is("andy [X001]"));
        assertThat(bean.getPrice(), is(300));
        assertThat(response.getHeaderString("x-id"), is(id));
    }

    @Test
    public void testUri() throws Exception {

        String rootPath = root.getUri().getPath();

        // @formatter:off
        URI uri = root.path("{0}/{last}")
                .resolveTemplate("0", "api")
                .resolveTemplate("last", "rs")
                .getUri();
        // @formatter:on

        assertThat(uri.getPath(), is(rootPath + "/api/rs"));
    }

    @Test
    public void testHypermedia() {

        Response response = root.path("link").request(MEDIA_TYPE).get();
        final String header = "<http://localhost:8080/sample-web/rs/demo/link>; rel=\"next\"";
        final String entity = "Bean(name=andy, type=DARK, price=200)";
        final int status = 200;

        assertThat(response.getHeaderString("Link"), is(header));
        assertThat(response.readEntity(Bean.class).toString(), is(entity));
        assertThat(response.getStatus(), is(status));

        Link link = response.getLink("next");

        response = client.target(link).request(MEDIA_TYPE)
                .header("x-id", "X001").get();

        assertThat(response.getHeaderString("Link"), is(header));
        assertThat(response.readEntity(Bean.class).toString(), is(entity));
        assertThat(response.getStatus(), is(status));
    }

    private static Entity<Bean> createEntity() {
        Bean origin = new Bean("andy", RoastType.DARK, 200);
        return Entity.entity(origin, MEDIA_TYPE);
    }

}
