/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * The environment of the simulation.
 */
interface Environment {
    /**
     * Add a Node to the environment.
     * @param node the node to add.
     */
    fun addNode(node: Node)

    /**
     * The list of the nodes in the environment.
     */
    val nodes: List<Node>
}
