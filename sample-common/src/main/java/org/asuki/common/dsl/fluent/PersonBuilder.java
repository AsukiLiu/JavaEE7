package org.asuki.common.dsl.fluent;


public interface PersonBuilder {

    public PersonBuilder firstName(String firstName);

    public PersonBuilder lastName(String lastName);

    public Person create();
}
