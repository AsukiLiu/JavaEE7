package org.asuki.web.servlet.listener;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

public class ReadListenerImpl implements ReadListener {

    private final ServletInputStream input;
    private final HttpServletResponse res;
    private final AsyncContext ac;

    private Queue<String> queue = new LinkedBlockingQueue<>();

    public ReadListenerImpl(ServletInputStream input, HttpServletResponse res,
            AsyncContext ac) {
        this.input = input;
        this.res = res;
        this.ac = ac;
    }

    @Override
    public void onDataAvailable() throws IOException {
        StringBuilder sb = new StringBuilder();
        int len = -1;
        byte b[] = new byte[1024];

        while (input.isReady() && (len = input.read(b)) != -1) {
            sb.append(new String(b, 0, len));
        }

        queue.add(sb.toString());
    }

    @Override
    public void onAllDataRead() throws IOException {
        ServletOutputStream output = res.getOutputStream();
        WriteListener writeListener = new WriteListenerImpl(output, queue, ac);
        output.setWriteListener(writeListener);
    }

    @Override
    public void onError(Throwable t) {
        ac.complete();
        t.printStackTrace();
    }
}