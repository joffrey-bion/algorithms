package org.hildan.algorithms.graphs.bfs

import java.util.HashSet
import java.util.LinkedList

sealed class BFSResult {

    data class Found(val path: List<Int>) : BFSResult() {
        val length = path.size - 1
    }

    object NoPath : BFSResult()
}

fun bfsPath(graph: Map<Int, List<Int>>, start: Int, goals: Set<Int>): BFSResult {
    val open = LinkedList<Int>()
    val closed = HashSet<Int>()
    val parents = mutableMapOf<Int, Int>()
    open.add(start)

    while (open.isNotEmpty()) {
        val current = open.poll()
        if (current in goals) {
            return BFSResult.Found(buildPath(start, current, parents))
        }
        closed.add(current)

        val neighbours = graph[current] ?: error("no outgoing edge list for node $current")
        neighbours.filterNot { it in closed }.forEach {
            parents[it] = current
            open.add(it)
        }
    }
    return BFSResult.NoPath
}

private fun buildPath(start: Int, end: Int, parents: MutableMap<Int, Int>): List<Int> = ArrayList<Int>().apply {
    var current = end
    while (current != start) {
        add(current)
        current = parents[current] ?: error("parent not found for node $current")
    }
    add(current)
}.reversed()
