package org.hildan.algorithms.graphs

data class Edge(val node1: Int, val node2: Int)

/**
 * Builds a graph based on the given edges, and ensures all ndoes
 */
fun buildFullGraph(
    nNodes: Int,
    edges: List<Edge>,
    directed: Boolean = true,
    reversed: Boolean = false
): Map<Int, Set<Int>> {
    val graph = mutableMapOf<Int, MutableSet<Int>>()
    // ensures we have all nodes in the graph (even with no edges)
    repeat(nNodes) {
        graph[it] = HashSet()
    }
    edges.forEach {
        if (!directed || !reversed) {
            graph.getValue(it.node1).add(it.node2)
        }
        if (!directed || reversed) {
            graph.getValue(it.node2).add(it.node1)
        }
    }
    return graph
}

fun buildGraph(
    edges: List<Edge>,
    directed: Boolean = true,
    reversed: Boolean = false
): Map<Int, Set<Int>> {
    val graph = mutableMapOf<Int, MutableSet<Int>>()
    edges.forEach {
        val node1Successors = graph.computeIfAbsent(it.node1) { HashSet() }
        val node2Successors = graph.computeIfAbsent(it.node2) { HashSet() }
        if (!directed || !reversed) {
            node1Successors.add(it.node2)
        }
        if (!directed || reversed) {
            node2Successors.add(it.node1)
        }
    }
    return graph
}

fun topologicalSort(reversedGraph: Map<Int, Set<Int>>): List<Int> {
    val mReversedGraph = reversedGraph.mapValuesTo(HashMap()) { it.value.toMutableSet() }
    val result = mutableListOf<Int>()
    while (mReversedGraph.isNotEmpty()) {
        val noParentNode = mReversedGraph.entries.first { it.value.isEmpty() }.key
        mReversedGraph.remove(noParentNode)
        mReversedGraph.values.forEach { it.remove(noParentNode) }
        result.add(noParentNode)
    }
    return result
}
