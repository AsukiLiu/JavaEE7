package org.asuki.web.servlet.listener;

import static java.lang.String.format;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;

@WebListener
public class EventListener implements ServletContextListener,
        HttpSessionAttributeListener, HttpSessionListener {

    private ServletContext context;

    @Inject
    private Logger log;

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        attribute(event, "attributeAdded");
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        attribute(event, "attributeRemoved");
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        attribute(event, "attributeReplaced");
    }

    private void attribute(HttpSessionBindingEvent event, String action) {
        log(format("Session id: %s, %s(%s : %s)", event.getSession().getId(),
                action, event.getName(), event.getValue()));
    }

    // The web application
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log("contextDestroyed");
        this.context = null;
    }

    // The web application
    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
        log("contextInitialized");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log("sessionCreated(" + event.getSession().getId() + ")");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        log("sessionDestroyed(" + event.getSession().getId() + ")");
    }

    protected void log(String message) {

        if (context != null) {
            context.log("EventListener: " + message);
        } else {
            log.info(message);
        }

    }

    protected void log(String message, Throwable throwable) {

        if (context != null) {
            context.log("EventListener: " + message, throwable);
        } else {
            log.info(message);
            throwable.printStackTrace(System.out);
        }

    }

}