package org.asuki.web.servlet;

import static java.lang.Thread.currentThread;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/async-concurrency", asyncSupported = true)
public class AsyncConcurrencyHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Resource
    private ManagedThreadFactory threadFactory;

    @Resource
    private ManagedExecutorService executor;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        final AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                log.info("onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.info("onTimeout");
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                log.info("onError");
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("onStartAsync");
            }
        });

        final PrintWriter out = resp.getWriter();

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // Ignore
            }

            asyncContext.complete();
            out.println("Completed");
            log.info(currentThread().getName());
        };

        threadFactory.newThread(task).start();

        executor.submit(task);

        log.info("Do another thing ...");
    }

}
