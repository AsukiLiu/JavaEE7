package org.asuki.web.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.web.servlet.wrapper.HeaderRequestWrapper;
import org.asuki.web.servlet.wrapper.CustomResponseWrapper;

@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class HeaderFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        Map<String, String> addHeaders = new HashMap<>();
        addHeaders.put("X-USER-ID", "1001");
        addHeaders.put("X-USER-NAME", "Andy");

        HeaderRequestWrapper wrappedRequest = new HeaderRequestWrapper(
                (HttpServletRequest) request, addHeaders);
        CustomResponseWrapper wrappedResponse = new CustomResponseWrapper(
                (HttpServletResponse) response);

        chain.doFilter(wrappedRequest, wrappedResponse);

        if (response.getContentType() == null
                || !response.getContentType().contains("text/html")) {
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            String outHtml = wrappedResponse.toString();
            outHtml += "<!-- Wrapper Test -->";

            response.setContentLength(outHtml.length());

            out.write(outHtml);
            out.flush();
        }
    }
}
