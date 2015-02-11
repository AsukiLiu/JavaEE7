package org.asuki.web.servlet;

import static java.lang.Thread.currentThread;
import static org.asuki.web.servlet.listener.AppContextListener.THREAD_POOL_NAME;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.asuki.web.servlet.listener.AppAsyncListener;
import org.slf4j.Logger;

// http://localhost:8080/sample-web/async-processing?time=3
@WebServlet(urlPatterns = "/async-processing", asyncSupported = true)
public class AsyncProcessingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final long TIME_OUT = 5_000L;

    @Inject
    private Logger log;

    @SneakyThrows
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        log.info("Start: Name={},ID={}", currentThread().getName(),
                currentThread().getId());

        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        String time = request.getParameter("time");
        int secs = Integer.valueOf(time);
        // longProcessing(secs);

        AsyncContext asyncCtx = request.startAsync();
        asyncCtx.addListener(new AppAsyncListener());
        asyncCtx.setTimeout(TIME_OUT);

        final CountDownLatch latch = new CountDownLatch(1);

        // Approach one
        asyncCtx.start(new AsyncProcessor(asyncCtx, secs, latch));

        // Approach two
        ThreadPoolExecutor executor = (ThreadPoolExecutor) request
                .getServletContext().getAttribute(THREAD_POOL_NAME);
        executor.execute(new AsyncProcessor(asyncCtx, secs, latch));

        long endTime = System.currentTimeMillis();
        log.info("End: name={}, id={}, time taken={}ms", currentThread()
                .getName(), currentThread().getId(), endTime - startTime);

        // response.setContentType("text/html;charset=UTF-8");
        // latch.await();
    }

    @AllArgsConstructor
    private class AsyncProcessor implements Runnable {

        private final AsyncContext asyncContext;
        private final int secs;
        private final CountDownLatch latch;

        @SneakyThrows
        @Override
        public void run() {
            log.info("Async Supported ? {}", asyncContext.getRequest()
                    .isAsyncSupported());

            longProcessing(secs);

            PrintWriter out = asyncContext.getResponse().getWriter();
            out.write(" Processing done! ");

            asyncContext.complete();

            latch.countDown();
        }

        @SneakyThrows
        private void longProcessing(int secs) {
            TimeUnit.SECONDS.sleep(secs);
        }
    }

}
