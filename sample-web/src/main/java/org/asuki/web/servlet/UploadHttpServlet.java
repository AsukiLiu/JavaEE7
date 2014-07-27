package org.asuki.web.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;

@WebServlet("/view/upload")
@MultipartConfig(maxFileSize = 1024)
public class UploadHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        Collection<Part> parts = null;

        try {
            parts = req.getParts();
        } catch (Exception e) {
            // maximum size exceeded etc.
            log.info(e.getMessage());
            return;
        }

        // Part part = request.getPart("file1");
        for (Part part : parts) {
            for (String h : part.getHeaderNames()) {
                log.info("{}:{}", h, part.getHeader(h));
            }

            log.info("Content type:{}", part.getContentType());
            log.info("Target name:{}", part.getName());
            log.info("Filename:{}", part.getSubmittedFileName());
            log.info("Size:{}bytes", part.getSize());

            if (part.getSize() == 0) {
                continue;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(part.getInputStream(), "UTF-8"))) {

                String line = null;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            part.write("/tmp/up-" + part.getSubmittedFileName());
        }

        req.setAttribute("message", "Uploaded successfully");
        getServletContext().getRequestDispatcher("/view/upload.xhtml").forward(
                req, resp);
    }
}
