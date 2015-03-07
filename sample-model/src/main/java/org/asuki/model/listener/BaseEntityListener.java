package org.asuki.model.listener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
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

        entity.setLocalDate(LocalDate.now());
        entity.setLocalDateTime(LocalDateTime.now());
        entity.setOffsetDateTime(OffsetDateTime.now());
        entity.setPeriod(Period.ofDays(20));
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setModifiedDate(new Date());
    }
}
