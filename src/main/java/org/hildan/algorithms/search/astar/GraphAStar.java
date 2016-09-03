package org.hildan.algorithms.search.astar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The graph represents an undirected graph.
 *
 * @param <T>
 */
final class GraphAStar<T> implements Iterable<T> {
    /**
     * A map from the nodeId to outgoing edge.
     * An outgoing edge is represented as a tuple of NodeData and the edge length
     */
    private final Map<T, Map<NodeData<T>, Double>> graph;

    /**
     * A map of heuristic from a node to each other node in the graph.
     */
    private final Map<T, Map<T, Double>> heuristicMap;

    /**
     * A map between nodeId and nodedata.
     */
    private final Map<T, NodeData<T>> nodeIdNodeData;

    public GraphAStar(Map<T, Map<T, Double>> heuristicMap) {
        if (heuristicMap == null) {
            throw new NullPointerException("The huerisic map should not be null");
        }
        graph = new HashMap<>();
        nodeIdNodeData = new HashMap<>();
        this.heuristicMap = heuristicMap;
    }

    /**
     * Adds a new node to the graph.
     * Internally it creates the nodeData and populates the heuristic map concerning input node into node data.
     *
     * @param nodeId
     *         the node to be added
     */
    public void addNode(T nodeId) {
        if (nodeId == null) {
            throw new IllegalArgumentException("Cannot add a null node");
        }
        if (!heuristicMap.containsKey(nodeId)) {
            throw new NoSuchElementException("This node is not a part of heuristic map");
        }

        graph.put(nodeId, new HashMap<>());
        nodeIdNodeData.put(nodeId, new NodeData<T>(nodeId, heuristicMap.get(nodeId)));
    }

    /**
     * Adds an edge from source node to destination node.
     * There can only be a single edge from source to node.
     * Adding additional edge would overwrite the value
     *
     * @param source
     *         the first node to be in the edge
     * @param destination
     *         the second node to be second node in the edge
     * @param length
     *         the length of the edge.
     */
    public void addEdge(T source, T destination, double length) {
        if (source == null || destination == null) {
            throw new NullPointerException("The first nor second node can be null.");
        }

        if (!heuristicMap.containsKey(source) || !heuristicMap.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of hueristic map");
        }
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source and Destination both should be part of the part of graph");
        }

        graph.get(source).put(nodeIdNodeData.get(destination), length);
    }

    /**
     * Returns immutable view of the edges
     *
     * @param nodeId
     *         the nodeId whose outgoing edge needs to be returned
     *
     * @return An immutable view of edges leaving that node
     */
    public Map<NodeData<T>, Double> edgesFrom(T nodeId) {
        if (nodeId == null) {
            throw new NullPointerException("The input node should not be null.");
        }
        if (!heuristicMap.containsKey(nodeId)) {
            throw new NoSuchElementException("This node is not a part of hueristic map");
        }
        if (!graph.containsKey(nodeId)) {
            throw new NoSuchElementException("The node should not be null.");
        }

        return Collections.unmodifiableMap(graph.get(nodeId));
    }

    /**
     * The nodedata corresponding to the current nodeId.
     *
     * @param nodeId
     *         the nodeId to be returned
     *
     * @return the nodeData from the
     */
    public NodeData<T> getNodeData(T nodeId) {
        if (nodeId == null) {
            throw new NullPointerException("The nodeid should not be empty");
        }
        if (!nodeIdNodeData.containsKey(nodeId)) {
            throw new NoSuchElementException("The nodeId does not exist");
        }
        return nodeIdNodeData.get(nodeId);
    }

    /**
     * Returns an iterator that can traverse the nodes of the graph
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return graph.keySet().iterator();
    }
}
