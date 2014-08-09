package org.asuki.web.servlet.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> filteredParams;

    public ParameterRequestWrapper(HttpServletRequest request,
            Map<String, String[]> filteredParams) {
        super(request);
        this.filteredParams = filteredParams;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return filteredParams;
    }

    @Override
    public String getParameter(String name) {
        return filteredParams.get(name) == null ? null : filteredParams
                .get(name)[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(filteredParams.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return filteredParams.get(name);
    }
}
