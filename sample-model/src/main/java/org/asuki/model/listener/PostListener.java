package org.asuki.model.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.asuki.model.entity.Post;

public class PostListener {

    @PrePersist
    public void prePresist(Post post) {
        final Date now = new Date();
        post.setCreatedDate(now);
        post.setModifiedDate(now);
    }

    @PreUpdate
    public void preUpdate(Post post) {
        post.setModifiedDate(new Date());
    }
}
