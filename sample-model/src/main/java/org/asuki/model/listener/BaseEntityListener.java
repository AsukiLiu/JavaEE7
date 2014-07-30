package org.asuki.model.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.asuki.model.entity.BaseEntity;

public class BaseEntityListener {

    @PrePersist
    public void prePresist(BaseEntity entity) {
        final Date now = new Date();
        entity.setCreatedDate(now);
        entity.setModifiedDate(now);
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setModifiedDate(new Date());
    }
}
