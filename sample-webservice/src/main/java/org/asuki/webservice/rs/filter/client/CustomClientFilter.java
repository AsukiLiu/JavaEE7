package org.asuki.webservice.rs.filter.client;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class CustomClientFilter implements ClientRequestFilter,
        ClientResponseFilter {

    private String user;
    private String password;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        MultivaluedMap<String, Object> headers = requestContext.getHeaders();

        headers.add("Authorization", getBasicAuthentication());
    }

    @Override
    public void filter(ClientRequestContext requestContext,
            ClientResponseContext responseContext) throws IOException {
        // TODO Auto-generated method stub

    }

    @SneakyThrows
    private String getBasicAuthentication() {
        String token = this.user + ":" + this.password;

        return "BASIC "
                + DatatypeConverter.printBase64Binary(token.getBytes(UTF_8));
    }
}