package org.asuki.batch;

import static java.util.Calendar.SECOND;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static org.asuki.common.Constants.Batchs.EXECUTION_DELAY;
import static org.asuki.common.Constants.Batchs.EXECUTION_TIMES;

import javax.annotation.Resource;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;

import java.util.Calendar;
import java.util.Date;

@Stateless
@Local(ManagedScheduledBatch.class)
public class ManagedScheduledBatchImpl implements ManagedScheduledBatch {

    @Resource
    private ManagedScheduledExecutorService executor;

    @Override
    public void runJob() {
        executor.schedule(createJob(), new Trigger() {

            @Override
            public Date getNextRunTime(LastExecution lastExecutionInfo,
                    Date taskScheduledTime) {

                if (Job.executedBatchs.size() >= EXECUTION_TIMES) {
                    return null;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(lastExecutionInfo == null ? taskScheduledTime
                        : lastExecutionInfo.getRunStart());
                cal.add(SECOND, EXECUTION_DELAY);
                return cal.getTime();
            }

            @Override
            public boolean skipRun(LastExecution lastExecutionInfo,
                    Date scheduledRunTime) {

                for (Long executedBatch : Job.executedBatchs) {
                    if (!BatchRuntime.getJobOperator()
                            .getJobExecution(executedBatch).getBatchStatus()
                            .equals(COMPLETED)) {
                        return true;
                    }
                }

                return false;
            }

        });
    }

    public void runJob_2() {
        executor.scheduleWithFixedDelay(createJob(), 1, EXECUTION_DELAY,
                SECONDS);
    }

    protected Job createJob() {
        return new Job();
    }
}
