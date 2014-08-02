package org.asuki.web.filter;

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

import org.asuki.web.servlet.wrapper.CustomRequestWrapper;
import org.asuki.web.servlet.wrapper.CustomResponseWrapper;

@WebFilter("/*")
public class WrapperFilter implements Filter {

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

        CustomRequestWrapper wrappedReq = new CustomRequestWrapper(
                (HttpServletRequest) request, addHeaders);
        CustomResponseWrapper wrappedResp = new CustomResponseWrapper(
                (HttpServletResponse) response);

        chain.doFilter(wrappedReq, wrappedResp);

        try (PrintWriter out = response.getWriter()) {
            String outHtml = wrappedResp.toString();
            outHtml += "<!-- Wrapper Test -->";

            response.setContentLength(outHtml.length());

            out.write(outHtml);
            out.flush();
        }
    }
}
