package org.asuki.webservice.rs.data;

import static org.asuki.webservice.rs.entity.User.builder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.asuki.webservice.rs.entity.User;

public class DummyUserDatabase {
    private static Map<Integer, User> users = new HashMap<>();

    static {
        // @formatter:off
        User user = builder()
                .id(1)
                .firstName("Andy")
                .lastName("L")
                .uri("/users/1")
                .lastModified(new Date())
            .build();
        // @formatter:on

        users.put(1, user);
    }

    public static User getUserById(Integer id) {
        return users.get(id);
    }

    public static User updateUser(Integer id) {
        User user = users.get(id);
        user.setLastModified(new Date());
        return user;
    }

    public static String getUserRole(String username, String password) {
        return "andy".equals(username) ? "ADMIN" : "GUEST";
    }

}
