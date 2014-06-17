package org.asuki.webservice.rs.entity;

import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingParams {

    @QueryParam("limit")
    private String limit;

    @QueryParam("offset")
    private String offset;

    @QueryParam("sort")
    private String sort;

    @QueryParam("direction")
    private Direction direction;

    public enum Direction {
        ASC, DESC;
    }

}
