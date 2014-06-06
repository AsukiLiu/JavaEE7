package org.asuki.common.dsl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Edge {

    private Vertex fromVertex;

    private Vertex toVertex;

    private Double weight;

}
