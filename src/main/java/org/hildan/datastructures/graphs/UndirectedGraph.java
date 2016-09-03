package org.hildan.datastructures.graphs;

import java.util.Map;

public class UndirectedGraph<I, D, C> extends SimpleGraph<I, C> {

    @Override
    public void addEdge(I sourceId, I destinationId, C cost) {
        super.addEdge(sourceId, destinationId, cost);
        super.addEdge(destinationId, sourceId, cost);
    }

    public Map<I, C> neighbours(I nodeId) {
        return edgesFrom(nodeId);
    }

}
