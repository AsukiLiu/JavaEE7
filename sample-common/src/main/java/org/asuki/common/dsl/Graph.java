package org.asuki.common.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Graph {

    @Getter
    private List<Edge> edges;

    @Getter
    private Set<Vertex> vertices;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new TreeSet<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

}