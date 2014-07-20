package org.asuki.web.servlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.concurrency.CustomManagedExecutorService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@WebServlet(urlPatterns = "/concurrency")
public class ConcurrencyServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomManagedExecutorService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            service.executeTasks();
        } catch (InterruptedException | ExecutionException e) {
            throw new ServletException("thread error");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        service.scheduleTasks();
    }

}
