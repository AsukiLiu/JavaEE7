package org.asuki.common.javase;

import org.asuki.common.javase.annotation.Hint;

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
// @Hints({ @Hint("hint1"), @Hint("hint2") }) //Before Java SE8
@Hint("hint1")
@Hint("hint2")
public class Person {

    private String name;

    private int age;

    public Person(String name) {
        this.name = name;
    }

}
