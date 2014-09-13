package org.asuki.common.javase.model;

public class Student extends Person {
    public Student(Person person) {
        super.setAge(person.getAge());
        super.setName(person.getName());
    }
}
