package org.asuki.webservice.rs.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String uri;

    private String firstName;

    private String lastName;

    private Date lastModified;

}
