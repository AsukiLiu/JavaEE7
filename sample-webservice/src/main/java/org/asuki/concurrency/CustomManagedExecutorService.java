package org.asuki.concurrency;

import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ejb.LockType.READ;

import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ContextService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
@Lock(READ)
public class CustomManagedExecutorService {

    @Inject
    private Logger log;

    @Resource
    private ManagedExecutorService executor;

    @Resource
    private ManagedScheduledExecutorService scheduledExecutor;

    @Resource
    private ContextService service;

    @Resource
    private ManagedThreadFactory threadFactory;

    private ScheduledFuture<?> future;

    public void executeTasks() throws InterruptedException, ExecutionException {

        List<Callable<String>> tasks = new ArrayList<>();
        tasks.add(new CallableTaskA(1));
        tasks.add(new CallableTaskB(2));

        //executor.submit(Callable/Runnable)
        List<Future<String>> taskResults = executor.invokeAll(tasks);

        List<String> results = new ArrayList<>();
        for (Future<String> taskResult : taskResults) {
            results.add(taskResult.get());
        }

        log.info(results.toString());
    }

    public void scheduleTasks() {

        if (future == null || future.isCancelled()) {
            future = scheduledExecutor.scheduleAtFixedRate(() -> {
                log.info("Date: " + new Date());
            }, 0, 3, SECONDS);

            return;
        }

        future.cancel(true);
    }

    public void executeByContextService() throws InterruptedException,
            ExecutionException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        @SuppressWarnings("unchecked")
        Callable<String> proxiedTask = service.createContextualProxy(
                new CallableTaskA(1), Callable.class);

        Future<String> result = executorService.submit(proxiedTask);
        log.info(result.get());
    }

    public void executeByThreadFactory() throws InterruptedException,
            ExecutionException {

        //threadFactory.newThread(Runnable).start();
        ExecutorService executorService = new ThreadPoolExecutor(3, 3, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                threadFactory);

        Future<String> result = executorService.submit(new CallableTaskB(2));
        log.info(result.get());
    }
}
