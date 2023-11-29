/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * An implementation of the environment.
 */
class EnvironmentImpl(private val linkingRule: LinkingRule) :
    Environment {

    override val nodes: ArrayList<Node> = ArrayList()
    override val nodeToPosition: HashMap<Int, Position> = HashMap()
    override val neighborhoods: ArrayList<Neighborhood> = ArrayList()
    private val nodeChangeFlow: MutableSharedFlow<NodeEvent> = MutableSharedFlow()

    override suspend fun addNode(node: Node, position: Position) {
        nodes.add(node)
        nodeToPosition[node.id] = position
        val neighborhood = SimpleNeighborhood(node, this, linkingRule, nodeChangeFlow)
        neighborhoods.add(neighborhood)
        nodeChangeFlow.emit(NodeEvent.NodeAdded(node))
    }

    override suspend fun removeNode(node: Node) {
        nodes.remove(node)
        nodeToPosition.remove(node.id)
        neighborhoods.remove(neighborhoods.find { it.getCenter() == node })
        nodeChangeFlow.emit(NodeEvent.NodeRemoved(node))
    }

    override suspend fun moveNode(node: Node, position: Position) {
        println("node ${node.id} moved in position $position")
        nodeToPosition[node.id] = position
        nodeChangeFlow.emit(NodeEvent.NodeMoved(node))
    }

    override fun getNodePosition(node: Node): Position {
        nodeToPosition[node.id].also {
            if (it == null) {
                val nodeExists: Boolean = nodes.contains(node)
                check(!nodeExists) {
                    ("Node $node is registered in the environment, but it has no position.")
                }
                throw IllegalArgumentException("Node ${node.id} does not exist in the environment.")
            } else {
                return it
            }
        }
    }

    override fun getNeighborhood(node: Node): Neighborhood {
        return neighborhoods.first { it.getCenter() == node }
    }
}
