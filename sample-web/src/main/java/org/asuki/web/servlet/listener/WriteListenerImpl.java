package org.asuki.web.servlet.listener;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class WriteListenerImpl implements WriteListener {

    private final ServletOutputStream output;
    private final Queue<String> queue;
    private final AsyncContext ac;

    public WriteListenerImpl(ServletOutputStream output, Queue<String> queue,
            AsyncContext ac) {
        this.output = output;
        this.queue = queue;
        this.ac = ac;
    }

    @Override
    public void onWritePossible() throws IOException {
        while (output.isReady() && queue.peek() != null) {
            output.print(queue.poll());
        }

        if (queue.peek() == null) {
            ac.complete();
        }
    }

    @Override
    public void onError(Throwable t) {
        ac.complete();
        t.printStackTrace();
    }
}
