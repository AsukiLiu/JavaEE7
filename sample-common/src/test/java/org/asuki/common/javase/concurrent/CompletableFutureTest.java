package org.asuki.common.javase.concurrent;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CompletableFutureTest {

    private List<String> list;

    @BeforeMethod
    public void setUp() {
        list = asList("A", "B", "C", "D");
    }

    @Test
    public void testThenApply() {
        list.stream()
                .map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.thenApply(n -> n + n))
                .map(f -> f.join())
                .forEach(out::println);
    }

    @Test
    public void testThenAccept() {
        list.stream()
                .map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.thenAccept(out::println))
                .map(f -> f.join())
                .count();
    }

    @Test
    public void testWhenComplete() {
        list.stream()
                .map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.whenComplete((rlt, err) -> out.println(rlt + " Error:" + err)))
                .count();
    }

    @Test
    public void testGetNow() {
        list.stream()
                .map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.getNow("Not Done"))
                .forEach(out::println);
    }

    @Test
    public void testThenCompose() throws Exception {
        List<String> sites = asList("www.google.com", "www.youtube.com", "www.yahoo.com");

        List<CompletableFuture<String>> futures = sites.stream()
                .map(site -> supplyAsync(() -> download(site)))
                .map(f -> f.thenApply(this::parse))
                .map(f -> f.thenCompose(this::analyze))
                .collect(Collectors.toList());

        for (CompletableFuture<String> future : futures) {
            out.println(future.get());
        }
    }

    private String download(String site) {
        return site + " downloaded";
    }

    private String parse(String site) {
        return site + " parsed";
    }

    private CompletableFuture<String> analyze(String site) {
        return supplyAsync(() -> site + " analyzed");
    }

}
