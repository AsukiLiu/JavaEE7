package org.asuki.webservice.rs.resource;

import static java.lang.String.format;
import static java.lang.String.join;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.PagingParams;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

//http://localhost:8080/sample-web/rs/param
@Path("param")
@Produces(TEXT_PLAIN)
public class ParamResource extends BaseResource {

    @Inject
    private Logger log;

    @Context
    private UriInfo uriInfo;

    // user/a01/xxx@gmail.com/090-9999-9999
    @Path("user/{id: [a-zA-Z][a-zA-Z0-9]*}/{mail}/{phone}")
    @GET
    public String pathParam(
            // @formatter:off
            @PathParam("id") String id,
            @PathParam("mail") String mail, 
            @PathParam("phone") String phone) {
            // @formatter:on

        Map<String, String> user = new HashMap<>();
        user.put("id", id);
        user.put("mail", mail);
        user.put("phone", phone);

        return user.toString();
    }

    // query?name=andy&age=20
    @GET
    @Path("query")
    public String queryParam(@QueryParam("name") String name,
            @QueryParam("age") int age,
            @DefaultValue("None") @QueryParam("job") String job) {

        return "name=" + name + "\nage=" + age + "\njob=" + job;
    }

    // page?offset=20&limit=50
    @Path("page")
    @GET
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
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

    // position;latitude=37.12;longitude=165.26
    // @formatter:off
    @Path("position")
    @GET
    public String matrixParam(
            @MatrixParam("latitude") double latitude,
            @MatrixParam("longitude") double longitude) {

        // 37.12N 165.26E
        return format("%3.2f%s %3.2f%s",
                Math.abs(latitude),
                latitude == 0.0 ? "" : (latitude > 0.0 ? "N" : "S"),
                Math.abs(longitude), 
                longitude == 0.0 ? "": (longitude > 0.0 ? "E" : "W"));
    }
    // @formatter:on

    @GET
    @Path("bean/{name}/{price}")
    public String beanParam(@BeanParam Bean bean) {

        return "name=" + bean.getName() + "\nprice=" + bean.getPrice();
    }

    @POST
    @Path("form")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String formParam(@FormParam("name") String name,
            @FormParam("age") int age) {

        return "name=" + name + "\nage=" + age;
    }

    @POST
    @Path("map")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String multivaluedMap(MultivaluedMap<String, String> map) {

        return mapToString(map);
    }

    // @formatter:off
        /*
        <bean>
            <name>andy</name>
            <type>DARK</type>
            <price>200</price>
        </bean>

        {
            "name":"andy",
            "type":"DARK",
            "price":200
        }
         */
     // @formatter:on
    @POST
    @Path("bean")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Bean beanEntity(Bean request) throws IOException {

        Bean response = request;

        return response;
    }

    @POST
    @Path("stream")
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Bean inputStream(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();

        try (InputStreamReader stream = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(stream)) {

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        Bean bean = mapper.readValue(sb.toString(), Bean.class);

        return bean;
    }

    @GET
    @Path("header")
    public String headerParam(@Context HttpHeaders header,
            @HeaderParam("User-Agent") String userAgent) {

        String agent = "User agent : " + userAgent;
        String path = "Path : " + uriInfo.getPath();

        return join("\n", agent, path, mapToString(header.getRequestHeaders()));
    }

    private static String mapToString(Map<String, List<String>> map) {
        StringBuilder sb = new StringBuilder();

        map.forEach((key, value) -> {
            sb.append(key).append(" :\n");
            value.forEach(item -> {
                sb.append("\t").append(item).append("\n");
            });
        });

        return sb.toString();
    }

}
