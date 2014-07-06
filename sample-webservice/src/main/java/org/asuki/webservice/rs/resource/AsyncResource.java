package org.asuki.webservice.rs.resource;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import lombok.SneakyThrows;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.RoastType;

public class AsyncResource {

    @SneakyThrows
    @POST
    @Consumes({ APPLICATION_JSON })
    @Produces({ APPLICATION_JSON })
    public void send(@Suspended AsyncResponse ar, Bean bean,
            @PathParam("id") String id) {

        TimeUnit.SECONDS.sleep(2);

        bean.setType(RoastType.LIGHT);
        bean.setName(format("%s [%s]", bean.getName(), id));
        bean.setPrice(bean.getPrice() + 100);

        Response response = Response.ok(bean).header("x-id", id).build();
        ar.resume(response);
    }
}
