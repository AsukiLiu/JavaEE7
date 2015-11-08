package org.asuki.retry;

import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.ejb.LockType.WRITE;

@Singleton
@Startup
public class TargetService {
    private String initTime;

    @PostConstruct
    public void init() {
        initTime = new Date().toString();
    }

    @SneakyThrows
    @AccessTimeout(value = 2, unit = SECONDS)
    @Lock(WRITE)
    public String get() {
        SECONDS.sleep(30);

        return initTime;
    }
}
