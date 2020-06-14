package org.hildan.algorithms.graphs.bfs

import java.util.HashSet
import java.util.LinkedList

fun bfsClosestGoal(graph: Map<Int, Set<Int>>, start: Int, goals: Set<Int>): Int? {
    val open = LinkedList<Int>()
    val closed = HashSet<Int>()
    open.add(start)

    while (open.isNotEmpty()) {
        val current = open.poll()
        if (current in goals) {
            return current
        }
        closed.add(current)

        val neighbours = graph[current] ?: error("no outgoing edge list for node $current")
        neighbours.filterNot { it in closed }.forEach { open.add(it) }
    }
    return null
}
