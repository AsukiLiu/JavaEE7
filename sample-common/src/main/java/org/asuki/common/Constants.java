package org.asuki.common;

import com.google.common.base.Charsets;

public interface Constants {

    interface Messages {
        String TEST_CONNECTION = "java:/JmsNoTx";
        String TEST_QUEUE = "java:/queues/testQueue";
    }

    interface Webs {
        String DEFAULT_CHARSET = Charsets.UTF_8.toString();
    }

    interface Sqls {
        String COUNT_COMMENTS_BY_ID = "COUNT_COMMENTS_BY_ID";
    }

    interface Batchs {
        String JOB_NAME = "myJob";
        int EXECUTION_DELAY = 3;
        int EXECUTION_TIMES = 3;
    }

}
