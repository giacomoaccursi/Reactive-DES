/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * An implementation of the environment.
 */
class EnvironmentImpl : Environment {
    override val nodes = ArrayList<Node>()
class EnvironmentImpl(override val nodes: ArrayList<Node> = ArrayList(), private val linkingRule: LinkingRule) :
    Environment {

    override val neighborhoods: ArrayList<Neighborhood> = ArrayList(
        nodes.map {
            SimpleNeighborhood(it, this, linkingRule)
        },
    )

    override fun addNode(node: Node) {
        nodes.add(node)
        neighborhoods.add(SimpleNeighborhood(node, this, linkingRule))
        nodeToPosition[node.id] = position
        val neighborhood = SimpleNeighborhood(node, this, linkingRule, nodeChangeFlow)
        neighborhoods.add(neighborhood)
    }

    override suspend fun removeNode(node: Node) {
        nodes.remove(node)
        nodeToPosition.remove(node.id)
        neighborhoods.remove(neighborhoods.find { it.getCenter() == node })
    override suspend fun moveNode(node: Node, position: Position) {
        println("node ${node.id} moved in position $position")
        nodeToPosition[node.id] = position
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
}
