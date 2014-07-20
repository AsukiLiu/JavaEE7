package org.asuki.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallableTaskA implements Callable<String> {

    private int taskId;

    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return "Task[" + taskId + "] completed";
    }
}
