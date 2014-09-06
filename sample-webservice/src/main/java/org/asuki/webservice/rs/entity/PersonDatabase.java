package org.asuki.webservice.rs.entity;

import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

public class PersonDatabase {

    public static ConcurrentMap<Integer, Person> persons = new ConcurrentHashMap<>();

    static {
        Person person = new Person();
        person.setId(1);
        person.setName("Andy");
        person.setLastModified(new Date());
        persons.put(person.getId(), person);
    }

    @SneakyThrows
    public static Person getPersonById(Integer id) {
        TimeUnit.SECONDS.sleep(2);

        return persons.get(id);
    }

    public static void updatePerson(Integer id) {
        Person person = persons.get(id);
        person.setLastModified(new Date());
    }

    public static Date getLastModifiedById(Integer id) {
        return persons.get(id).getLastModified();
    }

    public static int getSecond(Date date) {
        Calendar cal = getInstance();
        cal.setTime(date);
        return cal.get(SECOND);
    }
}
