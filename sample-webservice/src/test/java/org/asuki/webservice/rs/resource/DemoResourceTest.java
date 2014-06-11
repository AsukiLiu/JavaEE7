package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.net.URI;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.RoastType;
import org.asuki.webservice.rs.filter.client.CustomClientFilter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import lombok.SneakyThrows;

public class DemoResourceTest {

    private static final String MEDIA_TYPE = APPLICATION_XML;

    private Client client;
    private WebTarget root;
    private boolean isCompleted;

    @BeforeMethod
    public void init() {
        client = ClientBuilder.newClient().register(new CustomClientFilter("andy", "1234"));
        root = client.target("http://localhost:8080/sample-web/rs/demo");
    }

    @SneakyThrows
    @Test
    public void testCrud() {

        final Entity<Bean> entity = createEntity();
        final Bean bean = entity.getEntity();

        // @POST
        Response response = root.request().post(entity, Response.class);
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
        assertThat(bean.getBlend(), is("coffee, please."));
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

    private static Entity<Bean> createEntity() {
        Bean origin = new Bean("andy", RoastType.DARK, "coffee");
        return Entity.entity(origin, MEDIA_TYPE);
    }

}
