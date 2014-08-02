package org.asuki.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment_positive")
@DiscriminatorValue("P")
public class CommentPositive extends Comment {

    private static final long serialVersionUID = 1L;

    public CommentPositive(String content) {
        super(content);
    }

}
