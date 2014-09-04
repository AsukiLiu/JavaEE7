package org.asuki.webservice.rs.entity;

import java.util.Map;

//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// See JacksonJsonContextResolver.java
//@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor
public class JsonResponse {

    private static final float VERSION = 1.0f;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String errorMsg;

    @Getter
    @Setter
    private Map<String, Object> fieldErrors;

    @Getter
    @Setter
    private Object data;

    public JsonResponse(String status) {
        this.status = status;
    }

    public float getVersion() {
        return VERSION;
    }

}