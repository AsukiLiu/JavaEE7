package org.asuki.dao.redis;

import redis.clients.jedis.Jedis;

@FunctionalInterface
public interface Redisable<T> {

    T perform(Jedis jedis);
}
