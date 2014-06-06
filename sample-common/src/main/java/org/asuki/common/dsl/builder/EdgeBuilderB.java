package org.asuki.common.dsl.builder;

import org.asuki.common.dsl.Edge;
import org.asuki.common.dsl.Vertex;

public class EdgeBuilderB {

    public static Edge edge(Vertex from, Vertex to, Double weight) {

        return new Edge(from, to, weight);
    }

    public static Double weight(Double weight) {
        return weight;
    }

}
