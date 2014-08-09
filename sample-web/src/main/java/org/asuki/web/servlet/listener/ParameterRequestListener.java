package org.asuki.web.servlet.listener;

import static java.lang.String.join;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ParameterRequestListener implements ServletRequestListener {

    private final String LISTENER_NAME = getClass().getSimpleName();

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        Map<String, String[]> paramMap = sre.getServletRequest()
                .getParameterMap();
        ServletContext servletContext = sre.getServletContext();

        servletContext.log(LISTENER_NAME + " initialized");

        servletContext.log("Parameters size: " + paramMap.size());
        paramMap.forEach((key, value) -> servletContext.log(key + "="
                + join(",", value)));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        sre.getServletContext().log(LISTENER_NAME + " destroyed");
    }

}
