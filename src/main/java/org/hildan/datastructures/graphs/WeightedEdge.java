package org.hildan.datastructures.graphs;

/**
 * Represents a weighted oriented edge between two nodes.
 *
 * @param <N>
 *         the node type
 * @param <C>
 *         the cost type
 */
public class WeightedEdge<N, C> extends Edge<N> {

    private final C cost;

    public WeightedEdge(N from, N to, C cost) {
        super(from, to);
        if (cost == null) {
            throw new IllegalArgumentException("The cost of this edge cannot be null");
        }
        this.cost = cost;
    }

    public C getCost() {
        return cost;
    }
}
