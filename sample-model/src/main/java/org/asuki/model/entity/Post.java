package org.asuki.model.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.asuki.model.converter.UuidToBytesConverter;
import org.asuki.model.converter.ModeConverter;
import org.asuki.model.enums.Mode;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Wither;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Wither
@NamedEntityGraph(name = "post", attributeNodes = {
        @NamedAttributeNode("title"),
        @NamedAttributeNode(value = "comments", subgraph = "comments") }, subgraphs = { @NamedSubgraph(name = "comments", attributeNodes = { @NamedAttributeNode("content") }) })
public class Post extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
    @Convert(converter = UuidToBytesConverter.class)
    @Column
    private UUID uuid;

    @Getter
    @Setter
    @Column
    private boolean approved;

    @Getter
    @Setter
    @Convert(converter = ModeConverter.class)
    @Column
    private Mode mode;

    @Getter
    @Setter
    @OneToMany(cascade = ALL, mappedBy = "post", orphanRemoval = true, fetch = LAZY)
    private List<Comment> comments;

}
