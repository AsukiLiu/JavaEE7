package org.asuki.dao.redis;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.asuki.dao.redis.kv.Notification;

import com.google.gson.Gson;

@ApplicationScoped
public class NotificationDao {

    @Inject
    private RedisExecution redisExecution;

    public Long add(Notification notification) {
        return redisExecution.execute(jedis -> jedis.zadd("notifications:"
                + notification.getUsername(), notification.getTimestamp(),
                toJSON(notification)));
    }

    public Set<Notification> find(String username) {
        Set<String> notifications = redisExecution.execute(jedis -> jedis
                .zrevrangeByScore("notifications:" + username,
                        new Date().getTime(), 0));

        return notifications.stream().map(NotificationDao::toNotification)
                .collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
    }

    private String toJSON(Notification notification) {
        Gson gson = new Gson();
        return gson.toJson(notification);
    }

    private static Notification toNotification(String notification) {
        Gson gson = new Gson();
        return gson.fromJson(notification, Notification.class);
    }
}
