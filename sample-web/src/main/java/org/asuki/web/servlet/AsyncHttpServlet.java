package org.asuki.web.servlet;

import static org.asuki.common.Constants.Webs.DEFAULT_CHARSET;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = { "/async" }, loadOnStartup = 1, asyncSupported = true, initParams = {
        @WebInitParam(name = "name", value = "Andy"),
        @WebInitParam(name = "age", value = "20") })
public class AsyncHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding(DEFAULT_CHARSET);
        resp.setContentType("text/html;charset=utf-8");

        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(30 * 1000);
        asyncContext.start(new JobThread(asyncContext));

        log.info("Do another thing ...");

        PrintWriter out = resp.getWriter();
        ServletConfig config = getServletConfig();

        out.println(config.getInitParameter("name"));
        out.println(config.getInitParameter("age"));

        out.flush();
    }

}

class JobThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private AsyncContext asyncContext;

    public JobThread(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @SneakyThrows
    @Override
    public void run() {

        log.info("Do one thing ...");

        TimeUnit.SECONDS.sleep(3);

        asyncContext.dispatch("/index.xhtml");
    }
}
