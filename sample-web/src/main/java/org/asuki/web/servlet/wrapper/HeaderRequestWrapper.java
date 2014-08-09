package org.asuki.web.servlet.wrapper;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.toArray;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HeaderRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String> addHeaders;

    public HeaderRequestWrapper(HttpServletRequest request,
            Map<String, String> addHeaders) {

        super(request);

        this.addHeaders = firstNonNull(addHeaders,
                new HashMap<String, String>());
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name) != null ? super.getHeader(name)
                : addHeaders.get(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> headerNames = new ArrayList<>();

        Enumeration<String> currentHeaderNames = super.getHeaderNames();
        while (currentHeaderNames.hasMoreElements()) {
            headerNames.add(currentHeaderNames.nextElement());
        }

        String[] names = toArray(concat(headerNames, addHeaders.keySet()),
                String.class);
        return Collections.enumeration(asList(names));
    }
}
