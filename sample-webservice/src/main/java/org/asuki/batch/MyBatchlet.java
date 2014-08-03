package org.asuki.batch;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

@Named
public class MyBatchlet extends AbstractBatchlet {

    @Inject
    private Logger log;

    @Override
    public String process() {
        log.info("Running batchlet");

        return BatchStatus.COMPLETED.toString();
    }
}
