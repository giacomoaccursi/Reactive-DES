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
    override val neighborhoods: MutableStateFlow<Map<Int, Neighborhood>> = MutableStateFlow(emptyMap())

    override fun addNode(node: Node, position: Position) {
        nodes.value += node
        nodesToPosition.value += Pair(node.id, position)
        updateNeighborhood(node)
    }

    override fun removeNode(node: Node) {
        nodes.value -= node
        nodesToPosition.value = nodesToPosition.value.minus(node.id)
        neighborhoods.value[node.id]?.neighbors?.forEach {
            val nodeNeighborhood = getNeighborhood(it)
            if (nodeNeighborhood != null) {
                neighborhoods.value += Pair(it.id, nodeNeighborhood.removeNeighbor(node))
            }
        }
        neighborhoods.value = neighborhoods.value.minus(node.id)
    }

    override fun moveNode(node: Node, position: Position) {
        nodesToPosition.value += Pair(node.id, position)
        updateNeighborhood(node)
    }

    override fun getNodePosition(node: Node): Position {
        nodesToPosition.value[node.id].also {
            if (it == null) {
                val nodeExists = nodes.value.contains(node)
                check(!nodeExists) {
                    ("Node $node is registered in the environment, but it has no position.")
                }
                throw IllegalArgumentException("Node ${node.id} does not exist in the environment.")
            } else {
                return it
            }
        }
    }

    override fun getNeighborhood(node: Node): Neighborhood? {
        return neighborhoods.value[node.id]
    }

    override fun getNodeFromId(id: Int) = nodes.value.first { it.id == id }

    override fun getAllNodes() = nodes.value.filter {
        // it ensures that all nodes returned have a position.
        it.id in nodesToPosition.value.keys
    }

    private fun updateNeighborhood(node: Node) {
        val newNeighborhood = SimpleNeighborhood(node, linkingRule.computeNeighbors(node, this))
        val oldNeighborhood = getNeighborhood(node)
        if (oldNeighborhood != null) {
            val lostNeighbors = oldNeighborhood.neighbors - newNeighborhood.neighbors
            val gainedNeighbors = newNeighborhood.neighbors - oldNeighborhood.neighbors
            lostNeighbors.forEach { neighbor ->
                val nodeNeighborhood = getNeighborhood(neighbor)
                if (nodeNeighborhood != null) {
                    neighborhoods.value += Pair(neighbor.id, nodeNeighborhood.removeNeighbor(node))
                }
            }
            gainedNeighbors.forEach { neighbor ->
                val nodeNeighborhood = getNeighborhood(neighbor)
                if (nodeNeighborhood != null) {
                    neighborhoods.value += Pair(neighbor.id, nodeNeighborhood.addNeighbor(node))
                }
            }
        } else {
            newNeighborhood.neighbors.forEach { neighbor ->
                val nodeNeighborhood = getNeighborhood(neighbor)
                if (nodeNeighborhood != null) {
                    neighborhoods.value += Pair(neighbor.id, nodeNeighborhood.addNeighbor(node))
                }
            }
        }
        neighborhoods.value += Pair(node.id, newNeighborhood)
    }
}
