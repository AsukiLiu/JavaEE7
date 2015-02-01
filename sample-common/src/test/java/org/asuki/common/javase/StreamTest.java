package org.asuki.common.javase;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.SneakyThrows;
import lombok.ToString;

import org.asuki.common.javase.model.Person;
import org.asuki.common.javase.model.Student;
import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;

public class StreamTest {

    @Test
    public void testCaseA() {

        final List<String> list = ImmutableList.of("sa", "bb", "sc");

        List<String> oldWay = newArrayList();
        for (String str : list) {
            if (str.startsWith("s")) {
                oldWay.add(str.toUpperCase());
            }
        }

        // s -> s.toUpperCase()
        List<String> newWay = list.stream().filter(s -> s.startsWith("s"))
                .map(String::toUpperCase).collect(Collectors.toList());

        assertThat(oldWay, is(newWay));

        list.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a))
                .forEach(out::println);

        Predicate<String> predicate = (s) -> s.startsWith("b");

        assertThat(list.stream().anyMatch(predicate), is(true));

        assertThat(list.stream().allMatch(predicate), is(false));

        assertThat(list.stream().noneMatch(predicate), is(false));

        assertThat(list.stream().filter(predicate).count(), is(1L));

        Optional<String> reduced = list.stream().sorted()
                .reduce((s1, s2) -> s1 + "#" + s2);

        reduced.ifPresent(out::println);

        assertThat(reduced.get(), is("bb#sa#sc"));
    }

    @Test
    public void testCaseB() {

        // @formatter:off
        List<Person> persons = Arrays.asList(
                new Person("Tom", 20), 
                new Person("John", 40), 
                new Person("John", 30));
        // @formatter:on

        Optional<Person> minAgePerson = persons.stream().min(
                (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()));
        assertThat(minAgePerson.isPresent(), is(true));
        assertThat(minAgePerson.get().toString(),
                is("Person(name=Tom, age=20)"));

        Map<Integer, String> peoples = persons.stream().collect(
                Collectors.toMap(Person::getAge, Person::getName));
        assertThat(peoples.toString(), is("{20=Tom, 40=John, 30=John}"));

        ConcurrentMap<String, List<Person>> result = persons.stream()
                .parallel()
                .collect(Collectors.groupingByConcurrent(Person::getName));
        assertThat(result.get("Tom").size(), is(1));
        assertThat(result.get("John").size(), is(2));

        String nameString = persons.stream().map((p) -> p.getName())
                .collect(Collectors.joining(", "));

        assertThat(nameString, is("Tom, John, John"));

        List<String> names = persons.stream().map((p) -> p.getName())
                .distinct().collect(Collectors.toList());

        assertThat(names.size(), is(2));

        IntSummaryStatistics stats = persons.stream()
                .mapToInt((p) -> p.getAge()).summaryStatistics();

        assertThat(
                stats.toString(),
                is("IntSummaryStatistics{count=3, sum=90, min=20, average=30.000000, max=40}"));
    }

    @Test
    public void testCaseC() {

        Object[] powers = Stream.iterate(1.0, p -> p * 2)
                .peek(e -> out.println(e)).limit(10).toArray();
        assertThat(powers.length, is(10));

        List<String> words = Stream.of("a", "b", "b", "c", "a", "d").distinct()
                .collect(Collectors.toList());
        assertThat(words.toString(), is("[a, b, c, d]"));

        Optional<String> maxWord = words.stream().max(
                String::compareToIgnoreCase);
        assertThat(maxWord.isPresent(), is(true));
        assertThat(maxWord.get(), is("d"));

        Optional<String> startsWithC = words.stream()
                .filter(s -> s.startsWith("c")).findFirst();
        assertThat(startsWithC.isPresent(), is(true));

        startsWithC = words.stream().parallel().filter(s -> s.startsWith("c"))
                .findAny();
        assertThat(startsWithC.isPresent(), is(true));

        boolean isStartsWithC = words.stream().parallel()
                .anyMatch(s -> s.startsWith("c"));
        assertThat(isStartsWithC, is(true));

        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        // @formatter:off
        Map<String, String> languageNames = locales.collect(
            Collectors.toMap(
                locale -> locale.getDisplayLanguage(), 
                locale -> locale.getDisplayLanguage(locale),
                (oldValue, newValue) -> oldValue));
        // @formatter:on
        out.println(languageNames);

        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, List<Locale>> countryToLocales = locales.collect(Collectors
                .groupingBy(Locale::getCountry));
        List<Locale> swissLocales = countryToLocales.get("CH");
        assertThat(swissLocales.toString(), is("[fr_CH, de_CH, it_CH]"));
    }

    @Test
    public void testCaseD() {
        String[] strings = { "aaa", "bbb", "ccc" };

        List<String> list = Stream.of(strings).collect(ArrayList::new,
                ArrayList::add, ArrayList::addAll);

        assertThat(list.toString(), is("[aaa, bbb, ccc]"));

        String concat = Stream
                .of(strings)
                .collect(StringBuilder::new, StringBuilder::append,
                        StringBuilder::append).toString();

        assertThat(concat, is("aaabbbccc"));
    }

    @Test
    public void testYieldLike() throws NoSuchAlgorithmException {
        final int RANDOM_INTS = 5;

        try (Stream<Integer> randomInt = generateRandomIntStream()) {
            randomInt.limit(RANDOM_INTS).sorted().forEach(out::println);
        }
    }

    // Entity to DTO
    @Test
    public void testMapping() {
        List<Person> persons = newArrayList();
        persons.add(new Person("Tom", 30));
        persons.add(new Person("Jack", 25));

        List<PersonDto> personDtos1 = StreamSupport
                .stream(persons.spliterator(), false).map(PersonDto::new)
                .collect(Collectors.toList());

        List<PersonDto> personDtos2 = persons.stream().map(PersonDto::new)
                .collect(Collectors.toList());

        assertThat(personDtos1.toString(), is(personDtos2.toString()));
    }

    @ToString
    private static class PersonDto {

        private int age;
        private String name;

        public PersonDto(Person person) {
            this.name = person.getName();
            this.age = person.getAge();
        }
    }

    @Test
    public void testParallelCaseA() {
        Integer[] array = { 9, 5, 10 };
        Arrays.parallelSort(array);
        assertThat(asList(array).toString(), is("[5, 9, 10]"));

        List<Integer> numbers = ImmutableList.of(9, 5, 10);
        assertThat(numbers.stream().sorted().collect(Collectors.toList())
                .toString(), is("[5, 9, 10]"));
        assertThat(
                numbers.parallelStream().sorted().collect(Collectors.toList())
                        .toString(), is("[5, 9, 10]"));

        Map<Boolean, List<Integer>> groupBy = numbers.parallelStream().collect(
                Collectors.groupingBy(s -> s > 6));
        assertThat(groupBy.toString(), is("{false=[5], true=[9, 10]}"));

        Integer[] filtered = numbers.parallelStream().filter(s -> s > 6)
                .toArray(Integer[]::new);
        assertThat(asList(filtered).toString(), is("[9, 10]"));

        "hello world".chars().forEach(c -> out.print((char) c));
        out.println();
        // Not preserve the order
        "hello world".chars().parallel().forEach(c -> out.print((char) c));
        out.println();

        Stopwatch stopwatch = Stopwatch.createStarted();
        IntStream.range(0, 10).forEach(m -> longRunProcess(m));
        stopwatch.stop();
        out.printf(" Time: %s%n", stopwatch);

        stopwatch = Stopwatch.createStarted();
        IntStream.range(0, 10).parallel().forEach(m -> longRunProcess(m));
        stopwatch.stop();
        // <10s
        out.printf(" Time: %s%n", stopwatch);
    }

    @Test
    public void testParallelCaseB() {
        List<Person> persons = asList(new Person("Joe"), new Person("Jim"),
                new Person("John"));
        persons.forEach(p -> p.setAge(20));

        // @formatter:off
        
        // .map(person -> new Student(person))
        List<Student> students = persons.stream()
                .filter(p -> p.getAge() > 18)
                .map(Student::new)
                .collect(Collectors.toList());

        students = persons.stream()
                .parallel()
                .filter(p -> p.getAge() > 18)
                .sequential()
                .map(Student::new)
                .collect(Collectors.toCollection(ArrayList::new));
        
        // @formatter:on

        assertThat(
                students.toString(),
                is("[Student{name=Joe, age=20}, Student{name=Jim, age=20}, Student{name=John, age=20}]"));
    }

    @SneakyThrows
    private <T> void longRunProcess(T t) {
        TimeUnit.SECONDS.sleep(1);

        out.print(t);
    }

    private static Stream<Integer> generateRandomIntStream()
            throws NoSuchAlgorithmException {

        return Stream.generate(new Supplier<Integer>() {

            final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            boolean init = false;
            int num = 0;

            @Override
            public Integer get() {
                if (!init) {
                    random.setSeed(new Date().getTime());
                    init = true;
                    out.println("init");
                }
                final int nextInt = random.nextInt();
                out.println(format("Generated random %d: %d", num++, nextInt));
                return nextInt;
            }

        });
    }
}
