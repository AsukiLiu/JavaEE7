package org.asuki.web.servlet.listener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public static final String THREAD_POOL_NAME = "thread-pool";

    @Override
    public void contextInitialized(ServletContextEvent event) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 1000L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
        event.getServletContext().setAttribute(THREAD_POOL_NAME, executor);

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) event
                .getServletContext().getAttribute(THREAD_POOL_NAME);
        executor.shutdown();
    }

}