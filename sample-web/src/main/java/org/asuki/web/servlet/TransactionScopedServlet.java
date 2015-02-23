package org.asuki.web.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.web.service.MainService;

@WebServlet(urlPatterns = "/txScope")
public class TransactionScopedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private MainService service;

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        service.doSomething();

    }

}
