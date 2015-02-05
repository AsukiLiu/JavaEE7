package org.asuki.dao.redis.kv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Notification {

    private String username;
    private String notice;
    private long timestamp;

}
