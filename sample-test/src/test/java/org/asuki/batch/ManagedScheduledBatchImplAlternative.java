package org.asuki.batch;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

@Alternative
@Stateless
@Local(ManagedScheduledBatch.class)
public class ManagedScheduledBatchImplAlternative extends
        ManagedScheduledBatchImpl {

    @Override
    protected Job createJob() {
        return new JobAlternative();
    }
}
