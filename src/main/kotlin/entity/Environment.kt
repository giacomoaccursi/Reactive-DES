/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.Flow

/**
 * The environment of the simulation.
 */
interface Environment {
    /**
     * The list of the nodes in the environment.
     */
    val nodes: Flow<List<Node>>

    /**
     * The list of neighborhoods.
     */
    val neighborhoods: Flow<Map<Int, Neighborhood>>

    /**
     * Maps node id to its position.
     */
    val nodesToPosition: Flow<Map<Int, Position>>

    /**
     * Add a Node to the environment.
     * @param node the node to add.
     */
    fun addNode(node: Node, position: Position)

    /**
     * Remove node from the environment.
     * @param node the node to remove
     */
    fun removeNode(node: Node)

    /**
     * Moves node in a new position.
     * @param node the node to move.
     * @param position the new position.
     */
    fun moveNode(node: Node, position: Position)

    /**
     * @return the position of the node.
     */
    fun getNodePosition(node: Node): Position

    /**
     * @return the node's neighborhood.
     */
    fun getNeighborhood(node: Node): Neighborhood?

    /**
     * @return the node instance given its id.
     * @param id the id of the node.
     */
    fun getNodeFromId(id: Int): Node

    /**
     * @return all nodes in the environment.
     */
    fun getAllNodes(): List<Node>
}
