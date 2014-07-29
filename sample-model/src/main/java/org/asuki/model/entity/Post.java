package org.asuki.model.entity;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Converts;
import javax.persistence.EntityListeners;

import org.asuki.model.converter.UuidToBytesConverter;
import org.asuki.model.listener.PostListener;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;

@Entity
@Table(name = "post")
@Converts(value = { @Convert(attributeName = "uuid", converter = UuidToBytesConverter.class) })
@EntityListeners(PostListener.class)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Wither
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Getter
    @Setter
    @Column
    private String title;

    @Getter
    @Setter
    @Column
    private String body;

    @Getter
    @Setter
    // @Convert(converter = UuidToBytesConverter.class)
    @Column
    private UUID uuid;

    @Getter
    @Setter
    @Column
    private boolean approved;

    @Getter
    @Setter
    @Temporal(DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Getter
    @Setter
    @Temporal(DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

}
