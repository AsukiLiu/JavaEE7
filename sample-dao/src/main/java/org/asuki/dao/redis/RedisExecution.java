package org.asuki.dao.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RedisExecution {

    @Inject
    private JedisPool jedisPool;

    public <T> T execute(Redisable<T> operation) {
        Jedis jedis = jedisPool.getResource();
        try {
            return operation.perform(jedis);
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

}
