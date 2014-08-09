package org.asuki.web.servlet.filter;

import static java.util.Arrays.stream;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.asuki.web.servlet.wrapper.ParameterRequestWrapper;

@WebFilter("/*")
public class ParameterFilter implements Filter {

    private final String FILTER_NAME = getClass().getSimpleName();

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        servletContext.log(FILTER_NAME + " initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        final Map<String, String[]> filteredParams = Collections
                .unmodifiableMap(removeVowels(request.getParameterMap()));

        HttpServletRequestWrapper wrappedRequest = new ParameterRequestWrapper(
                (HttpServletRequest) request, filteredParams);

        try {
            servletContext.log(FILTER_NAME + " invoking...");

            chain.doFilter(wrappedRequest, response);
        } finally {
            servletContext.log(FILTER_NAME + " done");
        }
    }

    @Override
    public void destroy() {
        servletContext.log(FILTER_NAME + " destroyed");
        servletContext = null;
    }

    private Map<String, String[]> removeVowels(
            Map<String, String[]> parameterMap) {

        Map<String, String[]> result = new HashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            // @formatter:off
            List<String> vowelless = stream(entry.getValue())
                    .map(v -> v.replaceAll("[aeiou]", ""))
                    .collect(Collectors.toList());
            // @formatter:on

            result.put(entry.getKey(), vowelless.toArray(new String[] {}));
        }

        return result;
    }

}
