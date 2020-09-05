package org.hildan.algorithms.graphs.bfs

import java.util.HashSet
import java.util.LinkedList

private data class BfsMultiNode(val id: Int, val parent: BfsMultiNode?, val depth: Int)

fun bfsMultiPaths(graph: Map<Int, Set<Int>>, start: Int, goals: Set<Int>): Set<BFSResult.Found> {
    val open = LinkedList<BfsMultiNode>() // ArrayDeque is "experimental" and cannot be used on Codingame (1.3.x)
    val closed = HashSet<Int>()
    open.add(BfsMultiNode(start, null, 0))

    val results = mutableSetOf<BFSResult.Found>()
    var shortestPathLength: Int? = null

    while (open.isNotEmpty()) {
        val current = open.poll()
        if (shortestPathLength != null && current.depth > shortestPathLength) {
            return results
        }
        closed.add(current.id)
        if (current.id in goals) {
            shortestPathLength = current.depth
            results.add(BFSResult.Found(reconstructPathTo(current)))
            continue
        }

        val neighbours = graph[current.id] ?: error("no outgoing edge list for node $current")
        neighbours.filterNot { it in closed }.forEach {
            open.add(BfsMultiNode(it, current, current.depth + 1))
        }
    }
    return results
}

private fun reconstructPathTo(end: BfsMultiNode): List<Int> = ArrayList<Int>().apply {
    var current: BfsMultiNode? = end
    while (current != null) {
        add(current.id)
        current = current.parent
    }
}.reversed()
