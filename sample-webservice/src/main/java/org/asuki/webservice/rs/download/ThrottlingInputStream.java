package org.asuki.webservice.rs.download;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;

public class ThrottlingInputStream extends InputStream {

    private final InputStream stream;
    private final RateLimiter maxBytesPerSecond;

    public ThrottlingInputStream(InputStream target,
            RateLimiter maxBytesPerSecond) {
        this.stream = target;
        this.maxBytesPerSecond = maxBytesPerSecond;
    }

    @Override
    public int read() throws IOException {
        maxBytesPerSecond.acquire(1);
        return stream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        maxBytesPerSecond.acquire(b.length);
        return stream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        maxBytesPerSecond.acquire(len);
        return stream.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return stream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return stream.available();
    }

    @Override
    public synchronized void mark(int readlimit) {
        stream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        stream.reset();
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}