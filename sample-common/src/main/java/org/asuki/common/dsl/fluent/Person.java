package org.asuki.common.dsl.fluent;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class Person {

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    public static PersonBuilder with() {
        return FluentBuilder.create(new Person(), PersonBuilder.class);
    }

}