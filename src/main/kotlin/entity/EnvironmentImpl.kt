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
    }

    override fun removeNode(node: Node) {
        nodes.remove(node)
    }
}
