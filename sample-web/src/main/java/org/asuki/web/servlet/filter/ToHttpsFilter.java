package org.asuki.web.servlet.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

@WebFilter("/https/*")
public class ToHttpsFilter implements Filter {

    @Inject
    private Logger log;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        String url = httpReq.getRequestURL().toString();

        log.info("URL requested: " + url);

        if (httpReq.isSecure()) {
            chain.doFilter(req, res);
            return;
        }

        url = url.replaceFirst("http", "https")
                .replaceFirst(":8080/", ":8181/");

        if (httpReq.getQueryString() != null) {
            url += "?" + httpReq.getQueryString();
        }

        log.info("Redirect to: " + url);
        httpRes.sendRedirect(url);
    }

    @Override
    public void destroy() {
    }

}