package org.asuki.common.dsl.builder;

import org.asuki.common.dsl.Vertex;

public class VertexBuilderB {

    public static Vertex from(String label) {
        return new Vertex(label);
    }

    public static Vertex to(String label) {
        return new Vertex(label);
    }

}
