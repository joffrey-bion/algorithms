package org.hildan.datastructures.graphs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SimpleGraph<N, C> implements Graph<N, C> {

    private Collection<N> nodes = new HashSet<>();

    private Map<N, Map<N, C>> edgesFrom = new HashMap<>();

    private Map<N, Map<N, C>> edgesTo = new HashMap<>();

    /**
     * Creates an empty graph.
     */
    public SimpleGraph() {
    }

    /**
     * Creates a graph with the given nodes and edges.
     *
     * @param nodes
     *         the nodes to initialize the graph with. The given collection is copied, so this implementation guarantees
     *         not to add nor remove any node from it.
     * @param edges
     *         a collection of edges referring exclusively the given nodes. The given collection is copied, so this
     *         implementation guarantees not to add nor remove any edge from it.
     */
    public SimpleGraph(Collection<N> nodes, Collection<WeightedEdge<N, C>> edges) {
        addAllNodes(nodes);
        addAllEdges(edges);
    }

    /**
     * Creates a graph with the given nodes and edges.
     */
    public SimpleGraph(Collection<N> nodes, Map<N, Map<N, C>> edges) {
        this.nodes = nodes;
        this.edgesFrom.putAll(edges);
        edges.forEach((from, successors) -> {
            successors.forEach((to, cost) -> {
                edgesTo.putIfAbsent(to, new HashMap<>());
                edgesTo.get(to).put(from, cost);
            });
        });
    }

    public void addAllNodes(Collection<N> nodes) {
        nodes.forEach(this::addNode);
    }

    public void addAllEdges(Collection<WeightedEdge<N, C>> edges) {
        edges.forEach(this::addEdge);
    }

    public void addNode(N node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot add a null node");
        }
        nodes.add(node);
        edgesFrom.put(node, new HashMap<>());
        edgesTo.put(node, new HashMap<>());
    }

    public void addEdge(WeightedEdge<N, C> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Cannot add a null edge");
        }
        addEdge(edge.getFrom(), edge.getTo(), edge.getCost());
    }

    public void addEdge(N source, N destination, C cost) {
        if (!nodes.contains(source)) {
            throw new IllegalArgumentException("Unknown source node " + source);
        }
        if (!nodes.contains(destination)) {
            throw new IllegalArgumentException("Unknown destination node " + destination);
        }
        edgesFrom.get(source).put(destination, cost);
        edgesTo.get(destination).put(source, cost);
    }

    @Override
    public Collection<N> nodes() {
        return nodes;
    }

    @Override
    public Map<N, Map<N, C>> edges() {
        return edgesFrom;
    }

    @Override
    public Map<N, C> edgesFrom(N node) {
        return edgesFrom.get(node);
    }

    @Override
    public Map<N, C> edgesTo(N node) {
        return edgesTo.get(node);
    }
}
