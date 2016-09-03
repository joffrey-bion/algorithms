package org.hildan.datastructures.graphs;

import java.util.HashMap;
import java.util.Map;

public class SimpleDataGraph<I, D, C> extends SimpleGraph<I, C> implements DataGraph<I, D, C> {

    private Map<I, D> data = new HashMap<>();

    public void putDataNode(I nodeId, D nodeData) {
        if (nodeId == null) {
            throw new IllegalArgumentException("Cannot add a node with null id");
        }
        super.addNode(nodeId);
        data.put(nodeId, nodeData);
    }

    @Override
    public D getData(I nodeId) {
        return data.get(nodeId);
    }
}
