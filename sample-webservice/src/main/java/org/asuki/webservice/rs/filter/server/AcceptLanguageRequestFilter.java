package org.asuki.webservice.rs.filter.server;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.util.ThreadLocalUtil;

@Provider
public class AcceptLanguageRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        List<Locale> locales = requestContext.getAcceptableLanguages();
        if (!locales.isEmpty()) {
            ThreadLocalUtil.set(locales.get(0));
        }
    }
}
