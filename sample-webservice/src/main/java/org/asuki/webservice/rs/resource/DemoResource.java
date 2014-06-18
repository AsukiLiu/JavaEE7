package org.asuki.webservice.rs.resource;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.PagingParams;
import org.asuki.webservice.rs.entity.RoastType;
import org.slf4j.Logger;

//http://localhost:8080/sample-web/rs/demo
@ApplicationScoped
@Path("demo")
public class DemoResource extends BaseResource {

    @Inject
    private Logger log;

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

    // page?offset=20&limit=50
    @Path("page")
    @GET
    public PagingParams getPaging(@QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit, @Context UriInfo uriInfo,
            @Context ResourceContext rc) {

        log.info("Offset: " + offset);
        log.info("Limit: " + limit);

        log.info("Offset: " + uriInfo.getQueryParameters().getFirst("offset"));
        log.info("Limit: " + uriInfo.getQueryParameters().getFirst("limit"));

        PagingParams params = rc.getResource(PagingParams.class);
        log.info("Offset: " + params.getOffset());
        log.info("Limit: " + params.getLimit());

        return params;
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

    @GET
    @Path("csv")
    @Produces("text/csv")
    public String[][] getCsv() {

        String[] row = { "a", "bb", "ccc" };
        String[][] table = { row, row, row };

        return table;
    }

    // @formatter:off
    /*
    [
        {
            "Key1": "Value1",
            "Key2": "Value2",
            "Key3": "Value3"
        },
        {
            "KeyA": "ValueA",
            "KeyB": "ValueB",
            "KeyC": "ValueC"
        }
    ]
     */
    // @formatter:on
    @POST
    @Path("list")
    @Consumes({ APPLICATION_JSON })
    @Produces({ APPLICATION_JSON })
    public List<Map<String, String>> postList(List<Map<String, String>> input) {

        log.info(input.toString());

        Map<String, String> map = newLinkedHashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");

        return asList(map, map);
    }

}
