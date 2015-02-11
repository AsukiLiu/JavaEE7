package org.asuki.web.servlet.listener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppAsyncListener implements AsyncListener {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        log.info("onComplete");

        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("Completed");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        log.info("onTimeout");

        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("Timeout");
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        log.info("onError");
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        log.info("onStartAsync");
    }

}
