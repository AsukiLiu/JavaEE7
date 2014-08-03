package org.asuki.batch;

import static org.asuki.common.Constants.Batchs.EXECUTION_TIMES;
import static org.asuki.common.Constants.Batchs.EXECUTION_DELAY;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.concurrent.CountDownLatch;

@Startup
@Singleton
public class ScheduledBatchAlternative extends AbstractScheduledBatch {
    public static CountDownLatch countDownLatch = new CountDownLatch(
            EXECUTION_TIMES);

    @Override
    @Schedule(hour = "*", minute = "*", second = "*/" + EXECUTION_DELAY)
    public void myJob() {
        super.myJob();
    }

    @Override
    protected void afterRun() {
        countDownLatch.countDown();
    }
}
