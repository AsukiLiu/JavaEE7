package org.asuki.web.service;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.AsyncContext;

import org.slf4j.Logger;

import lombok.SneakyThrows;

@Stateless
public class AsyncService {

    @Inject
    private Logger log;

    @SneakyThrows
    @Asynchronous
    public void doSomething(AsyncContext asyncContext) {

        TimeUnit.SECONDS.sleep(5);

        try (PrintWriter out = asyncContext.getResponse().getWriter()) {
            out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            out.flush();
        }

        asyncContext.complete();
        log.info("Service done");
    }
}
