package org.hildan.algorithms.graphs.dijkstra

import java.util.HashSet
import java.util.PriorityQueue

data class DirectedEdge(
    val dest: Int,
    val length: Int
)

sealed class DijkstraResult {

    data class Found(val length: Int, val path: List<Int>) : DijkstraResult()

    object NoPath : DijkstraResult()
}

class Dijkstra(
    private val outgoingEdges: Map<Int, List<DirectedEdge>>
) {
    private data class PrioritizedNode(
        val id: Int,
        val distanceFromSource: Int
    )

    fun shortestPath(start: Int, goals: Set<Int>): DijkstraResult {
        val open = PriorityQueue<PrioritizedNode>(compareBy { it.distanceFromSource })
        val closed = HashSet<Int>()
        val distances = mutableMapOf<Int, Int>()
        val parents = mutableMapOf<Int, Int>()

        distances[start] = 0
        open.add(PrioritizedNode(start, 0))

        while (open.isNotEmpty()) {
            val current = open.poll()
            if (current.id in goals) {
                return DijkstraResult.Found(
                    distances.getValue(current.id), buildPath(start, current.id, parents)
                )
            }
            closed.add(current.id)
            val currentDist = distances[current.id] ?: Int.MAX_VALUE
            for (e in current.outgoingEdges()) {
                if (e.dest in closed) {
                    continue
                }
                val neighbour = e.dest
                val oldDist = distances[neighbour] ?: Int.MAX_VALUE
                val newDist = currentDist + e.length
                val shortestDist = minOf(oldDist, newDist)
                if (newDist < oldDist) {
                    distances[neighbour] = shortestDist
                    parents[neighbour] = current.id
                    open.add(PrioritizedNode(neighbour, shortestDist))
                }
            }
        }
        return DijkstraResult.NoPath
    }

    private fun PrioritizedNode.outgoingEdges(): List<DirectedEdge> = outgoingEdges[id] ?: error("no outgoing edge list for node $id")

    private fun buildPath(start: Int, end: Int, parents: MutableMap<Int, Int>): List<Int> = ArrayList<Int>().apply {
        var current = end
        while (current != start) {
            add(current)
            current = parents[current] ?: error("parent not found for node $current")
        }
        add(current)
    }.reversed()
}
