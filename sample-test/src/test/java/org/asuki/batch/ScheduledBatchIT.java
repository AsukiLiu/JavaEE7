package org.asuki.batch;

import static org.asuki.common.Constants.Batchs.EXECUTION_TIMES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.asuki.common.Constants;
import org.asuki.common.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.BatchRuntime;
import javax.inject.Inject;

@RunWith(Arquillian.class)
public class ScheduledBatchIT {

    @Inject
    private ManagedScheduledBatch managedScheduledBatch;

    @Deployment
    public static WebArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        // @formatter:off
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(
                        Resources.class,
                        Constants.class,
                        MyBatchlet.class, 
                        AbstractScheduledBatch.class,
                        ScheduledBatchAlternative.class,
                        Job.class,
                        JobAlternative.class, 
                        ManagedScheduledBatch.class,
                        ManagedScheduledBatchImpl.class,
                        ManagedScheduledBatchImplAlternative.class)
                .addAsWebInfResource(
                        new StringAsset(beansXml
                                .createAlternatives()
                                .clazz(ManagedScheduledBatchImplAlternative.class
                                        .getName()).up().exportAsString()),
                        beansXml.getDescriptorName())
                .addAsResource("META-INF/batch-jobs/myJob.xml");
        // @formatter:on

        System.out.println(war.toString(true));
        return war;
    }

    @Test
    @InSequence(1)
    public void testBatchByConcurrency() throws InterruptedException {
        managedScheduledBatch.runJob();

        checkResult(JobAlternative.countDownLatch,
                JobAlternative.executedBatchs);
    }

    @Test
    @InSequence(2)
    public void testBatchByEjb() throws InterruptedException {
        checkResult(ScheduledBatchAlternative.countDownLatch,
                ScheduledBatchAlternative.executedBatchs);
    }

    private void checkResult(CountDownLatch latch, List<Long> executedBatchs)
            throws InterruptedException {

        latch.await(20, SECONDS);

        assertThat(latch.getCount(), is(0L));
        assertThat(executedBatchs.size(), is(EXECUTION_TIMES));

        for (Long executedBatch : executedBatchs) {
            assertThat(
                    BatchRuntime.getJobOperator()
                            .getJobExecution(executedBatch).getBatchStatus(),
                    is(COMPLETED));
        }
    }
}
