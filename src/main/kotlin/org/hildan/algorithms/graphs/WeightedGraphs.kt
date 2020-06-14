package org.hildan.algorithms.graphs

data class WeightedEdge(val node1: Int, val node2: Int, val weight: Int)

data class OutgoingEdge(val target: Int, val weight: Int)

fun buildGraph(nNodes: Int, edges: List<WeightedEdge>, directed: Boolean): Map<Int, Set<OutgoingEdge>> {
    val graph = mutableMapOf<Int, MutableSet<OutgoingEdge>>()
    // ensures we have all nodes in the graph (even with no edges)
    repeat(nNodes) {
        graph[it] = HashSet()
    }
    edges.forEach {
        graph.getValue(it.node1).add(OutgoingEdge(it.node2, it.weight))
        if (!directed) {
            graph.getValue(it.node2).add(OutgoingEdge(it.node1, it.weight))
        }
    }
    return graph
}

fun topologicalSort(graph: Map<Int, Set<OutgoingEdge>>): List<Int> = graph.keys.sortedWith(Comparator { n1, n2 ->
    when {
        graph.getValue(n2).any { n1 == it.target } -> -1
        graph.getValue(n1).any { n2 == it.target } -> 1
        else -> 0
    }
})
