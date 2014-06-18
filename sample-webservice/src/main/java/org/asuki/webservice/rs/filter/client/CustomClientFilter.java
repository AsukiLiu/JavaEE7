package org.asuki.webservice.rs.filter.client;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.SneakyThrows;

public class CustomClientFilter implements ClientRequestFilter,
        ClientResponseFilter {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String user;
    private String password;

    public CustomClientFilter(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        log.info("ClientRequestFilter");

        MultivaluedMap<String, Object> headers = requestContext.getHeaders();

        headers.add("Authorization", getBasicAuthentication());
    }

    @Override
    public void filter(ClientRequestContext requestContext,
            ClientResponseContext responseContext) throws IOException {

        log.info("ClientResponseFilter");

        // TODO Auto-generated method stub

    }

    @SneakyThrows
    private String getBasicAuthentication() {
        String token = this.user + ":" + this.password;

        return "BASIC "
                + DatatypeConverter.printBase64Binary(token.getBytes(UTF_8));
    }
}