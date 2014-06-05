package org.asuki.common.javase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Person {

    private String name;

    private int age;

    public Person(String name) {
        this.name = name;
    }

}
