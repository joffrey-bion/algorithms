package org.hildan.algorithms.mcts.simultaneous

typealias Player = Int
typealias Score = Int

interface Action

interface State {

    val isTerminal: Boolean

    /**
     * Throws if not terminal.
     */
    fun score(): Score

    fun listValidActions(): Map<Player, List<Action>>

    fun transition(playerAction: Action, opponentAction: Action): State
}

class Node(
    val state: State
) {
    val children: MutableMap<Action, MutableMap<Action, Node>> = mutableMapOf()
    var totalScore: Int = 0
    var visits: Int = 0
}

class SimultaneousMoveMCTS {

    fun runWhile(stillTime: () -> Boolean): Action {
        TODO()
    }

    private fun runIteration(node: Node): Score {
        if (node.state.isTerminal) {
            return node.state.score()
        } else if (node.expansionRequired()) {

            // maybe combine all this
            val (playerAction, opponentAction) = node.findUnexploredActionPair()
            val newState = node.state.transition(playerAction, opponentAction)
            val newNode = Node(newState)
            node.addChild(newState)


            val score = playout(newNode)
            newNode.totalScore += score
            newNode.visits += 1
            update(node, playerAction, opponentAction, score)
        }

        val (playerAction, opponentAction) = select(node)
        val newState = node.state.transition(playerAction, opponentAction)
    }

    private fun Node.expansionRequired(): Boolean =
        TODO("check if some combination of actions are missing from children")

    private fun Node.findUnexploredActionPair(): Pair<Action, Action> =
        TODO()

    private fun select(node: Node): Pair<Action, Action> {
        TODO("DUCT algo")
    }
}

fun monteCarloTreeSearch(state: State) {

}