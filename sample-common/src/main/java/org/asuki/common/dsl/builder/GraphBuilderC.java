package org.asuki.common.dsl.builder;

import java.util.function.Consumer;

import org.asuki.common.dsl.Edge;
import org.asuki.common.dsl.Graph;

public class GraphBuilderC {

    private Graph graph;

    public GraphBuilderC() {
        graph = new Graph();
    }

    public static Graph Graph(Consumer<GraphBuilderC> gConsumer) {

        GraphBuilderC gBuilder = new GraphBuilderC();
        gConsumer.accept(gBuilder);

        return gBuilder.graph;
    }

    public void edge(Consumer<EdgeBuilderC> eConsumer) {

        EdgeBuilderC eBuilder = new EdgeBuilderC();
        eConsumer.accept(eBuilder);

        Edge edge = eBuilder.edge();
        graph.addEdge(edge);
        graph.addVertex(edge.getFromVertex());
        graph.addVertex(edge.getToVertex());
    }
}
