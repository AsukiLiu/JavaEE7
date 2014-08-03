package org.asuki.batch;

import static org.asuki.common.Constants.Batchs.EXECUTION_TIMES;

import java.util.concurrent.CountDownLatch;

public class JobAlternative extends Job {
    public static CountDownLatch countDownLatch = new CountDownLatch(
            EXECUTION_TIMES);

    @Override
    protected void afterRun() {
        countDownLatch.countDown();
    }
}
