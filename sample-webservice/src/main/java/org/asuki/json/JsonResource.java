package org.asuki.json;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;

@ApplicationScoped
public class JsonResource {

    @Inject
    private JsonBuilderFactory builderFactory;

    public JsonArray collectionToJsonArray() {

        // dummy
        List<CustomObject> list = asList(new CustomObject("101"),
                new CustomObject("102"));

        // @formatter:off
        return list
                .stream()
                .map(o -> o.id)
                .collect(
                        builderFactory::createArrayBuilder,
                        JsonArrayBuilder::add,
                        JsonArrayBuilder::add)
                .build();
        // @formatter:on
    }

    @Produces
    private JsonBuilderFactory produceJsonBuilderFactory() {
        return Json.createBuilderFactory(Collections.emptyMap());
    }

    private static class CustomObject {

        public String id;

        public CustomObject(String id) {
            this.id = id;
        }
    }
}
