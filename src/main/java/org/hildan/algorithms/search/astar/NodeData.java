package org.hildan.algorithms.search.astar;

import java.util.Map;

/**
 * Contains node-specific data for AStar algorithm.
 *
 * @param <I>
 *         the node ID type
 */
final class NodeData<I> implements Comparable<NodeData<I>> {

    private final I nodeId;

    private final Map<I, Double> heuristic;

    private double g;  // g is distance from the source

    private double f;  // f = g + h

    public NodeData(I nodeId, Map<I, Double> heuristic) {
        this.nodeId = nodeId;
        this.g = Double.MAX_VALUE;
        this.heuristic = heuristic;
    }

    public I getNodeId() {
        return nodeId;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void calcF(I destination) {
        double h = heuristic.get(destination);
        this.f = g + h;
    }

    @Override
    public int compareTo(NodeData<I> that) {
        if (this.getF() > that.getF()) {
            return 1;
        }
        if (this.getF() > that.getF()) {
            return -1;
        }
        return 0;
    }
}
