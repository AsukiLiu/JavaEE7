package org.asuki.common.javase;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

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

    @Test
    public void testMapExtension() {

        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "val" + i);
        }

        map.forEach((key, value) -> out
                .printf("Key: %s Value: %s ", key, value));

        // Delete
        map.computeIfPresent(4, (key, value) -> null);
        assertThat(map.containsKey(4), is(false));

        map.computeIfPresent(3, (key, value) -> value + key);
        assertThat(map.get(3), is("val33"));

        map.computeIfAbsent(3, value -> "aaa");
        assertThat(map.get(3), is("val33"));

        map.computeIfAbsent(12, value -> "aaa");
        assertThat(map.get(12), is("aaa"));

        map.remove(3, "val3");
        assertThat(map.get(3), is("val33"));

        map.remove(3, "val33");
        assertThat(map.containsKey(3), is(false));

        assertThat(map.getOrDefault(10, "not found"), is("not found"));

        map.merge(9, "aaa", (value, newValue) -> value.concat(newValue));
        assertThat(map.get(9), is("val9aaa"));
    }

    @Test
    public void testPredicate() {

        Predicate<String> predicate = (s) -> s.length() > 0;

        assertThat(predicate.test("foo"), is(true));
        assertThat(predicate.negate().test("foo"), is(false));

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        assertThat(nonNull.test(null), is(false));
        assertThat(isNull.test(null), is(true));

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();

        assertThat(isEmpty.test(""), is(true));
        assertThat(isNotEmpty.test(""), is(false));
    }

    @Test
    public void testFunction() {
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> toString = toInteger.andThen(String::valueOf);

        assertThat(toInteger.apply("123"), is(123));
        assertThat(toString.apply("123"), is("123"));
    }

    @Test
    public void testSupplier() {

        final String expected = "Person(name=Andy, age=0)";

        Supplier<Person> personSupplier = Person::new;
        Person person = personSupplier.get();
        person.setName("Andy");

        assertThat(person.toString(), is(expected));

        PersonFactory<Person> personFactory = Person::new;
        person = personFactory.create("Andy");

        assertThat(person.toString(), is(expected));
    }

    @Test
    public void testConsumer() {
        Consumer<Person> consumer = (p) -> p.setName("Mr. " + p.getName());
        Person person = new Person("Andy");

        consumer.accept(person);

        assertThat(person.toString(), is("Person(name=Mr. Andy, age=0)"));
    }

    @Test
    public void testComparator() {
        Person person1 = new Person("Tom");
        Person person2 = new Person("Mike");
        Person person3 = new Person("John");
        Person person4 = new Person("Tom");

        Comparator<Person> comparator = (p1, p2) -> p1.getName().compareTo(
                p2.getName());

        assertThat(comparator.compare(person1, person2) > 0, is(true));
        assertThat(comparator.reversed().compare(person1, person2) > 0,
                is(false));
        assertThat(comparator.reversed().compare(person1, person4) == 0,
                is(true));

        List<Person> persons = Arrays.asList(person1, person2, person3);
        Collections.sort(persons, comparator);

        persons.forEach((s) -> out.println(s));
        persons.forEach(out::println);
    }

    @Test
    public void testOptional() {
        Optional<String> optional = Optional.of("abc");

        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), is("abc"));
        assertThat(optional.orElse("yyy"), is("abc"));

        optional.ifPresent((s) -> out.println(s.charAt(0)));
    }

    @Test
    public void testCustomFunction() {
        // (from) -> Integer.valueOf(from)
        Converter<String, Integer> valueOf = Integer::valueOf;

        assertThat(valueOf.convert("123"), is(123));

        MyClass instance = new MyClass();
        Converter<String, String> startsWith = instance::startsWith;

        assertThat(startsWith.convert("Java"), is("J"));
    }

    @Test
    public void testStreamCaseA() {

        final List<String> list = ImmutableList.of("sa", "bb", "sc");

        List<String> oldWay = newArrayList();
        for (String str : list) {
            if (str.startsWith("s")) {
                oldWay.add(str.toUpperCase());
            }
        }

        // s -> s.toUpperCase()
        List<String> newWay = list.stream()
                .filter(s -> s.startsWith("s"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(oldWay, is(newWay));

        list.stream()
            .map(String::toUpperCase)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(out::println);

        Predicate<String> predicate = (s) -> s.startsWith("b");

        assertThat(list.stream().anyMatch(predicate), is(true));

        assertThat(list.stream().allMatch(predicate), is(false));

        assertThat(list.stream().noneMatch(predicate), is(false));

        assertThat(list.stream().filter(predicate).count(), is(1L));

        Optional<String> reduced = list.stream()
                .sorted()
                .reduce((s1, s2) -> s1 + "#" + s2);

        reduced.ifPresent(out::println);

        assertThat(reduced.get(), is("bb#sa#sc"));
    }

    @Test
    public void testStreamCaseB() {

        List<Person> persons = Arrays.asList(
                new Person("Tom", 20), 
                new Person("John", 40), 
                new Person("John", 30));

        String nameString = persons.stream()
                .map((p) -> p.getName())
                .collect(Collectors.joining(", "));

        assertThat(nameString, is("Tom, John, John"));

        List<String> names = persons.stream()
                .map((p) -> p.getName())
                .distinct()
                .collect(Collectors.toList());

        assertThat(names.size(), is(2));

        IntSummaryStatistics stats = persons.stream()
                .mapToInt((p) -> p.getAge())
                .summaryStatistics();

        assertThat(
                stats.toString(),
                is("IntSummaryStatistics{count=3, sum=90, min=20, average=30.000000, max=40}"));
    }

}

@FunctionalInterface
interface Converter<F, T> {
    T convert(F from);
}

class MyClass {
    public String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
