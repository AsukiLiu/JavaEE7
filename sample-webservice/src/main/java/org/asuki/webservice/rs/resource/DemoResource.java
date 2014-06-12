package org.asuki.webservice.rs.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.RoastType;

@ApplicationScoped
@Path("demo")
public class DemoResource extends BaseResource {

    @Context
    private ResourceContext rc;

    private Map<String, Bean> savedBeans;

    @PostConstruct
    public void init() {
        this.savedBeans = new ConcurrentHashMap<>();
    }

    @GET
    public Collection<Bean> allBeans() {
        return savedBeans.values();
    }

    @GET
    @Path("{id}")
    public Bean getById(@PathParam("id") String id) {
        return savedBeans.get(id);
    }

    @GET
    @Path("query")
    public Bean queryById(@QueryParam("id") String id) {
        return savedBeans.get(id);
    }

    @POST
    public Response add(Bean bean) {

        checkNotNull(bean);

        savedBeans.put(bean.getName(), bean);

        final URI id = URI.create(bean.getName());
        return Response.created(id).build();
    }

    @GET
    @Path("link")
    public Response getLink() {

        Bean bean = new Bean("andy", RoastType.DARK, "coffee");

        return Response.ok(bean)
                .link("http://localhost:8080/sample-web/rs/demo/link", "next")
                .build();
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        savedBeans.remove(id);
    }

    @Path("/async/{id}")
    public AsyncResource asyn() {
        return rc.initResource(new AsyncResource());
    }
}
