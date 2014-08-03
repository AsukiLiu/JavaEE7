package org.asuki.batch;

import static org.asuki.common.Constants.Batchs.JOB_NAME;

import javax.batch.runtime.BatchRuntime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Job implements Runnable {
    public static List<Long> executedBatchs = new ArrayList<>();

    public void run() {
        executedBatchs.add(BatchRuntime.getJobOperator().start(JOB_NAME,
                new Properties()));
        afterRun();
    }

    protected void afterRun() {
    }
}
