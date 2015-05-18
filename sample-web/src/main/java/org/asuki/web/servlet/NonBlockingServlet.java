package org.asuki.web.servlet;

import static java.lang.System.out;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.web.servlet.listener.ReadListenerImpl;

@WebServlet(urlPatterns = "/nonblock", asyncSupported = true)
public class NonBlockingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        AsyncContext ac = req.startAsync();
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                out.println("Complete");
            }

            @Override
            public void onError(AsyncEvent event) {
                out.println(event.getThrowable());
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                out.println("AsyncListener timeout");
            }
        });

        ServletInputStream input = req.getInputStream();
        ReadListener readListener = new ReadListenerImpl(input, res, ac);
        input.setReadListener(readListener);
    }
}
