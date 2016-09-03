package org.hildan.datastructures.graphs;

public interface DataGraph<I, D, C> extends Graph<I, C> {

    D getData(I nodeId);
}
