package org.hildan.datastructures.graphs;

/**
 * Represents an oriented edge between two nodes.
 *
 * @param <N>
 *         the node's type
 */
public class Edge<N> {

    private final N from;

    private final N to;

    public Edge(N from, N to) {
        if (from == null) {
            throw new IllegalArgumentException("Source node cannot be null");
        }
        if (to == null) {
            throw new IllegalArgumentException("Destination node cannot be null");
        }
        this.from = from;
        this.to = to;
    }

    public N getFrom() {
        return from;
    }

    public N getTo() {
        return to;
    }
}
