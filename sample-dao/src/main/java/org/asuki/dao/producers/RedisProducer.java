package org.asuki.dao.producers;

import static org.apache.commons.pool.impl.GenericObjectPool.WHEN_EXHAUSTED_GROW;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class RedisProducer {

    @Inject
    private Logger log;

    @PreDestroy
    public void close() {
        createJedisPool().destroy();
    }

    @Produces
    public JedisPool createJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(128);
        config.setMaxIdle(20);
        config.setTestWhileIdle(true);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        config.setTestOnReturn(true);
        config.setMinIdle(10);
        config.setWhenExhaustedAction(WHEN_EXHAUSTED_GROW);

        return new JedisPool(config, "localhost");
    }

    private JedisPool createJedisPool(String host, int port, String password,
            JedisPoolConfig config) {

        log.info("Redis connection (host: {}, port: {}, password: {})", host,
                port, password);
        return new JedisPool(config, host, port, 2000, password);
    }
}
