package org.asuki.common.dsl.fluent;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    public static PersonBuilder with() {
        return FluentBuilder.create(new Person(), PersonBuilder.class);
    }

}