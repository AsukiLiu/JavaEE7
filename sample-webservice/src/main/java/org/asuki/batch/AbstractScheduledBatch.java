package org.asuki.batch;

import static org.asuki.common.Constants.Batchs.JOB_NAME;

import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractScheduledBatch {
    public static List<Long> executedBatchs = new ArrayList<>();

    @Schedule(hour = "*", minute = "0", second = "0")
    public void myJob() {
        executedBatchs.add(BatchRuntime.getJobOperator().start(JOB_NAME,
                new Properties()));
        afterRun();
    }

    protected void afterRun() {
    }
}
