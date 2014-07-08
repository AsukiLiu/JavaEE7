package org.asuki.common.dsl.fluent;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Setter
    private String firstName;

    @JsonProperty
    @Setter
    private String lastName;

    public static PersonBuilder with() {
        return FluentBuilder.create(new Person(), PersonBuilder.class);
    }

}