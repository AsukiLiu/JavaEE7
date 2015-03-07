package org.asuki.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import org.asuki.model.listener.BaseEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private Long id;

    @Temporal(TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    /*
     * Java SE 8 (java.time)
     */

    @Column(name = "local_date")
    private LocalDate localDate;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "offset_date_time")
    private OffsetDateTime offsetDateTime;

    @Column(name = "period")
    private Period period;

}
