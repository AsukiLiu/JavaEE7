package org.asuki.common.javase;

import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import lombok.SneakyThrows;

import org.testng.annotations.Test;

public class Javase8Test {

    @Test
    public void testJoinString() {

        final String expected = "aaa,bbb";
        final String separator = ",";

        String csv = "";

        StringJoiner joiner = new StringJoiner(separator);
        joiner.add("aaa").add("bbb");

        csv = joiner.toString();
        assertThat(csv, is(expected));

        csv = String.join(separator, "aaa", "bbb");
        assertThat(csv, is(expected));

        List<String> strings = Arrays.asList("aaa", "bbb");

        csv = strings.stream().reduce((t, u) -> t + separator + u).get();
        assertThat(csv, is(expected));
    }

    @SneakyThrows
    @Test
    public void testLambda() {

        Callable<Double> pi = () -> 3.14;
        assertThat(pi.call(), is(3.14));

        String[] words = { "a", "bbb", "cc" };
        Arrays.sort(words, (s1, s2) -> s1.length() - s2.length());

        assertThat(words, is(new String[] { "a", "cc", "bbb" }));

        List<Person> persons = Arrays.asList(new Person("Tom"), new Person(
                "Mike"), new Person("John"));
        Collections.sort(persons, (p1, p2) -> p1.name.compareTo(p2.name));

        persons.forEach((s) -> out.println(s));
        persons.forEach(out::println);

        PersonFactory<Person> personFactory = Person::new;
        Person person = personFactory.create("Andy");

        assertThat(person.toString(), is("Person(name=Andy)"));
    }

    @Test
    public void testDefaultMethod() {

        // Formula formula = (num) -> sqrt(num * 100); // Compile error
        Formula formula = new Formula() {
            @Override
            public double calculate(int num) {
                return sqrt(num * 100);
            }
        };

        assertThat(formula.calculate(100), is(100.0));
        assertThat(formula.sqrt(16), is(4.0));
    }

}
