package org.asuki.rx;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.IntStream.range;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

import org.testng.annotations.Test;

import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.functions.Action2;

public class RxJavaTest {

    private static final int NUMBER_OF_TIMES = 5;

    private String sum;

    // @formatter:off
    private Action2<Subscriber<? super String>, String> doAction = (subscriber, id) -> {
        range(0, NUMBER_OF_TIMES).forEach(m -> {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(id + "_" + m);
            };
        });

        if (!subscriber.isUnsubscribed()) {
            subscriber.onCompleted();
        }
    };
    // @formatter:on

    @Test
    public void testSynchronousObservable() {
        List<String> result = new ArrayList<>();

        getSynchronousObservable().subscribe(it -> result.add(it));

        result.add("Sync done");

        assertThat(
                result,
                is(contains("sync_0", "sync_1", "sync_2", "sync_3", "sync_4",
                        "Sync done")));
    }

    private Observable<String> getSynchronousObservable() {
        return Observable.create((OnSubscribe<String>) subscriber -> doAction
                .call(subscriber, "sync"));
    }

    @SneakyThrows
    @Test
    public void testAsynchronousObservable() {
        List<String> result = new ArrayList<>();

        getAsynchronousObservable().subscribe(it -> result.add(it));

        result.add("Async done");

        TimeUnit.SECONDS.sleep(1);

        assertThat(
                result,
                is(contains("Async done", "async_0", "async_1", "async_2",
                        "async_3", "async_4")));
    }

    private Observable<String> getAsynchronousObservable() {
        return Observable.create((OnSubscribe<String>) subscriber -> {
            final Thread t = new Thread(() -> doAction
                    .call(subscriber, "async"));

            t.start();
        });
    }

    @Test
    public void testSkipTake() {
        List<String> result = new ArrayList<>();

        getSynchronousObservable().skip(1).take(3).map(value -> value + "_x")
                .subscribe(it -> {
                    result.add("next => " + it);
                });

        assertThat(
                result,
                is(contains("next => sync_1_x", "next => sync_2_x",
                        "next => sync_3_x")));
    }

    @Test
    public void testFilterReduceMap() {
        Observable<Integer> target = Observable.from(1, 2, 3, 4, 5);

        List<Integer> filterInts = new ArrayList<>();

        target.filter(v -> v < 4).subscribe(value -> filterInts.add(value));

        assertThat(filterInts, is(contains(1, 2, 3)));

        target.reduce((seed, value) -> seed + value).map(v -> "Sum: " + v)
                .subscribe(value -> sum = value);

        assertThat(sum, is("Sum: 15"));
    }

    @Test
    public void testOnMethods() {
        List<String> result = new ArrayList<>();

        String fileName = "sample.txt";
        readFile(fileName).skip(1).take(2).map(s -> ">" + s)
                .subscribe(s -> result.add(s));

        assertThat(result, is(contains(">line1", ">line2")));
    }

    private static Observable<String> readFile(String file) {
        return Observable.create((OnSubscribe<String>) subscriber -> {
            try (BufferedReader reader = Files.newBufferedReader(
                    Paths.get(file), UTF_8)) {

                String line = null;
                while ((line = reader.readLine()) != null) {
                    subscriber.onNext(line);
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

}
