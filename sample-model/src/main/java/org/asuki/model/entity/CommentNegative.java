package org.asuki.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment_negative")
@DiscriminatorValue("N")
public class CommentNegative extends Comment {

    private static final long serialVersionUID = 1L;

    public CommentNegative(String content) {
        super(content);
    }

}
