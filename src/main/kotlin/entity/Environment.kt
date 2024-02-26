/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.AwaitableMutableStateFlow

/**
 * The environment of the simulation.
 */
interface Environment {
    /**
     * The list of the nodes in the environment.
     */
    val nodes: AwaitableMutableStateFlow<List<Node>>

    /**
     * The list of neighborhoods.
     */
    val neighborhoods: AwaitableMutableStateFlow<Map<Int, Neighborhood>>

    /**
     * Maps every node with its position.
     */
    val nodesToPosition: AwaitableMutableStateFlow<Map<Int, Position>>

    /**
     * Add a Node to the environment.
     * @param node the node to add.
     */
    suspend fun addNode(node: Node, position: Position)

    /**
     * Remove node from the environment.
     * @param node the node to remove
     */
    suspend fun removeNode(node: Node)

    /**
     * Moves node in a new position.
     * @param node the node to move.
     * @param position the new position.
     */
    suspend fun moveNode(node: Node, position: Position)

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

    /**
     * Updates all neighborhoods.
     * @param neighborhoods all new neighborhoods.
     */
    suspend fun updateNeighborhoods(neighborhoods: Map<Int, Neighborhood>)

    /**
     * @param linkingRule the linking rule.
     */
    fun setLinkingRule(linkingRule: LinkingRule)
}
