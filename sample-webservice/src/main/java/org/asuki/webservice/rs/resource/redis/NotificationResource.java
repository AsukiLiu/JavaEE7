package org.asuki.webservice.rs.resource.redis;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.asuki.dao.redis.NotificationDao;
import org.asuki.dao.redis.kv.Notification;
import org.asuki.webservice.rs.filter.annotation.Authenticate;
import org.asuki.webservice.rs.filter.annotation.EnableSession;
import org.slf4j.Logger;

//http://localhost:8080/sample-web/rs/notifications
@Path("notifications")
public class NotificationResource {

    @Inject
    private Logger log;

    @Context
    private SecurityContext securityContext;

    @Inject
    private NotificationDao notificationDao;

    @Path("/crud")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @PermitAll
    @EnableSession
    @Authenticate
    public Response crud(NotificationRequest notificationRequest) {

        String username = securityContext.getUserPrincipal().getName();

        notificationDao.add(new Notification(username, notificationRequest
                .getNotice(), new Date().getTime()));

        Set<Notification> notifications = notificationDao.find(username);

        Map<String, String> json = new HashMap<>();
        json.put("msg", format("Successfully [%s %s]", username, notifications));
        return status(Response.Status.OK).entity(json).build();
    }

}
