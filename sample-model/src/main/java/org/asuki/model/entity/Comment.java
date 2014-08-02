package org.asuki.model.entity;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "post" })
@EqualsAndHashCode(callSuper = false)
@Wither
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name="comment_type")
public class Comment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Column
    private String content;

    @Getter
    @Setter
    @ManyToOne()
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content) {
        this.content = content;
    }

}
