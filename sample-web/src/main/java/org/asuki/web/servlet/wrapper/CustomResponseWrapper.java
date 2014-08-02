package org.asuki.web.servlet.wrapper;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CustomResponseWrapper extends HttpServletResponseWrapper {

    private CharArrayWriter caw;

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
        caw = new CharArrayWriter();
    }

    @Override
    public String toString() {
        return caw.toString();
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(caw);
    }
}
