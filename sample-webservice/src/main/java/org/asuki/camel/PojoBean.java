package org.asuki.camel;

import static org.asuki.camel.CamelRestService.URI;

import org.apache.camel.Consume;
import org.asuki.camel.dto.Input;

public class PojoBean {
    @Consume(uri = URI)
    public int process(Input request) {
        switch (request.getInData()) {
        case "A":
            return 1;
        case "B":
            return 2;
        default:
            return -1;
        }
    }
}
