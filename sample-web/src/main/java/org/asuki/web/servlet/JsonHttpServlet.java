package org.asuki.web.servlet;

import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createGenerator;
import static javax.json.Json.createObjectBuilder;
import static javax.json.Json.createWriter;

import java.io.IOException;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.json.JsonResource;

@WebServlet("/json")
public class JsonHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private JsonResource jsonResource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        
        // Object Model API
        try (JsonWriter jsonWriter = createWriter(resp.getWriter())) {

            // @formatter:off
            JsonObject jsonObject = createObjectBuilder()
                    .add("title", "Algorithm")
                    .add("isMember", true)
                    .add("price", 12.34d)
                    .add("ids", jsonResource.collectionToJsonArray())
                    .add("authors",
                        createArrayBuilder()
                            .add(createObjectBuilder()
                                .add("name", "Robby")
                                .add("age", 30))
                            .add(createObjectBuilder()
                                .add("name", "Zak")
                                .add("age", 20)))
                    .build();
            // @formatter:on

            jsonWriter.writeObject(jsonObject);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        // Streaming API
        try (JsonGenerator generator = createGenerator(resp.getWriter())) {

            // @formatter:off
            generator
                .writeStartObject()
                    .write("title", "Algorithm")
                    .write("isMember", true)
                    .writeStartArray("authors")
                        .write("Robby")
                        .write("Zak")
                    .writeEnd()
                    .write("price", 12.34d)
                .writeEnd();
            // @formatter:on
        }
    }

}