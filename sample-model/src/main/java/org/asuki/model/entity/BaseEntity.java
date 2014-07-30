package org.asuki.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
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

    @Temporal(DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

}
