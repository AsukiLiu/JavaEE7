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

}
