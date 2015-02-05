package org.asuki.webservice.rs.resource.mongo;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.asuki.dao.mongo.ProfileDao;
import org.asuki.webservice.rs.filter.annotation.AfterLogin;
import org.asuki.webservice.rs.filter.annotation.EnableSession;
import org.asuki.webservice.rs.filter.annotation.Authenticate;
import org.slf4j.Logger;

//http://localhost:8080/sample-web/rs/profiles
@Path("profiles")
public class ProfileResource {

    @Inject
    private Logger log;

    @Context
    private SecurityContext securityContext;

    @Inject
    private ProfileDao profileDao;

    @Path("/crud")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @PermitAll
    // @RolesAllowed("admin")
    @EnableSession
    @Authenticate
    @AfterLogin
    public Response crud(ProfileRequest profileRequest) {

        if (!securityContext.isUserInRole("admin")) {
            throw new WebApplicationException("Authorization failed", 403);
        }

        String username = securityContext.getUserPrincipal().getName();
        String detail = profileRequest.getDetail();

        profileDao.create(username, detail);

        boolean isExisted = profileDao.isExisted(username, detail);
        log.info("isExisted: {}", isExisted);

        profileDao.updateByPush(username, detail);

        profileDao.updateByPull(username, detail);

        profileDao.remove(username);

        Map<String, String> json = new HashMap<>();
        json.put("msg", format("Successfully [%s %s]", username, detail));
        return Response.status(Response.Status.OK).entity(json).build();
    }

}
