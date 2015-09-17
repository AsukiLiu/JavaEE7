package org.asuki.common.javase.concurrent;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    public void testBasic1() throws Exception {
        CompletableFuture<Integer> future1 = supplyAsync(() -> {
            if (false) {
                throw new RuntimeException();
            }
            return 40;
        });

        CompletableFuture<Integer> future2 = future1.thenApply(r -> r * r)
                .exceptionally(e -> 2);

        System.out.println(future2.join()); // 1600 2

        CompletableFuture<Integer> future3 = future1
                .handle((r, e) -> e == null ? r * r : 2);

        System.out.println(future3.get()); // 1600 2

        CompletableFuture<Integer> future4 = future1
                .whenComplete((r, e) -> System.out.println(r + 10)); // 50

        System.out.println(future4.get()); // 40

        CompletableFuture<Integer> future5 = future1
                .thenCompose(v -> supplyAsync(() -> v + 20));

        System.out.println(future5.get()); // 60
    }

    @Test
    public void testBasic2() throws Exception {
        CompletableFuture<Integer> future1 = supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return 40;
        });

        CompletableFuture<Integer> future2 = supplyAsync(() -> 20);

        CompletableFuture<Integer> future3 = future1.applyToEither(future2,
                r -> r * r);
        System.out.println(future3.get()); // 20 * 20

        CompletableFuture<Integer> future4 = future1.thenCombine(future2, (r1,
                r2) -> r1 + r2);
        System.out.println(future4.get()); // 40 + 20

        CompletableFuture<Void> future5 = future1.thenAcceptBoth(future2, (r1,
                r2) -> System.out.println(r1 + r2 + 10)); // 40 + 20 + 10

        System.out.println(future5.get()); // null
    }

    @Test
    public void testThenApply() {
        list.stream().map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.thenApply(n -> n + n)).map(f -> f.join())
                .forEach(out::println);
    }

    @Test
    public void testThenAccept() {
        list.stream().map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.thenAccept(out::println)).map(f -> f.join())
                .count();
    }

    @Test
    public void testWhenComplete() {
        list.stream()
                .map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.whenComplete((rlt, err) -> out.println(rlt
                        + " Error:" + err))).count();
    }

    @Test
    public void testGetNow() {
        list.stream().map(s -> supplyAsync(() -> s.toLowerCase()))
                .map(f -> f.getNow("Not Done")).forEach(out::println);
    }

    @Test
    public void testThenCompose() throws Exception {
        List<String> sites = asList("www.google.com", "www.youtube.com",
                "www.yahoo.com");

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
