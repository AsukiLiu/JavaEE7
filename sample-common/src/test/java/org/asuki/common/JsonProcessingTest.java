package org.asuki.common;

import static java.lang.System.out;
import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createObjectBuilder;
import static javax.json.Json.createParser;
import static javax.json.Json.createReader;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.StringReader;
import java.math.BigDecimal;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.testng.annotations.Test;

public class JsonProcessingTest {

    @Test
    public void testObjectModelAPI() {
        String jsonString = buildBook();
        out.println(jsonString);

        try (JsonReader jsonReader = createReader(new StringReader(jsonString))) {
            assertThat(jsonReader.readObject().toString(), is(jsonString));
        }

    }

    @Test
    public void testStreamingAPI() {
        String jsonString = buildBook();

        try (JsonParser parser = createParser(new StringReader(jsonString))) {

            while (parser.hasNext()) {
                final Event event = parser.next();
                switch (event) {
                case KEY_NAME:
                    String key = parser.getString();
                    out.println(key);
                    break;
                case VALUE_STRING:
                    String string = parser.getString();
                    out.println(" " + string);
                    break;
                case VALUE_NUMBER:
                    BigDecimal number = parser.getBigDecimal();
                    out.println(" " + number);
                    break;
                default:
                    break;
                }
            }

        }

    }

    private static String buildBook() {
        // @formatter:off
        JsonObject jsonObject = createObjectBuilder()
                .add("book",
                    createObjectBuilder()
                        .add("title", "Algorithm")
                        .add("isMember", true)
                        .add("authors",
                                createArrayBuilder()
                                    .add("Robby")
                                    .add("Zak"))
                        .add("price", 12.34d))
                .build();
        // @formatter:on

        return jsonObject.getJsonObject("book").toString();
    }
}
