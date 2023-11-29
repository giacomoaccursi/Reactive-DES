/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * An implementation of the environment.
 */
class EnvironmentImpl(private val linkingRule: LinkingRule) :
    Environment {

    override val nodes: MutableStateFlow<List<Node>> = MutableStateFlow(emptyList())
    override val nodesToPosition: MutableStateFlow<Map<Int, Position>> = MutableStateFlow(emptyMap())
    override val neighborhoods: ArrayList<Neighborhood> = ArrayList()

    override fun addNode(node: Node, position: Position) {
        nodes.value += node
        nodesToPosition.value += Pair(node.id, position)
        val neighborhood = SimpleNeighborhood(node, this, linkingRule)
        neighborhoods.add(neighborhood)
    }

    override fun removeNode(node: Node) {
        neighborhoods.remove(neighborhoods.find { it.getCenter() == node })
        nodes.value -= node
        nodesToPosition.value = nodesToPosition.value.filterKeys { it != node.id }
    }

    override fun moveNode(node: Node, position: Position) {
        nodesToPosition.value += Pair(node.id, position)
    }

    override fun getNodePosition(node: Node): Position {
        nodesToPosition.value[node.id].also {
            if (it == null) {
                val nodeExists: Boolean = nodes.value.contains(node)
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

    override fun getNodeFromId(id: Int) = nodes.value.first { it.id == id }

    override fun getAllNodes() = nodes.value.filter {
        // it ensures that all nodes returned have a position.
        it.id in nodesToPosition.value.keys
    }
}
