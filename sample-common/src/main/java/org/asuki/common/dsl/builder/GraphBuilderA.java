package org.asuki.common.dsl.builder;

import lombok.Getter;

import org.asuki.common.dsl.Graph;

public class GraphBuilderA {

    @Getter
    private Graph graph;

    public GraphBuilderA() {
        graph = new Graph();
    }

    public static GraphBuilderA Graph() {
        return new GraphBuilderA();
    }

    public EdgeBuilderA edge() {

        EdgeBuilderA ebuilder = new EdgeBuilderA(this);
        graph.addEdge(ebuilder.getEdge());
        return ebuilder;
    }

}