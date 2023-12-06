/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Interface for a neighborhood.
 */
interface Neighborhood {

    /**
     * The center of the neighborhood.
     */
    val center: Node

    /**
     * The neighbors of the node.
     */
    val neighbors: Set<Node>

    /**
     * Add a node neighbor.
     * @param node the new neighbor.
     * @return a new neighborhood plus the provided node.
     */
    fun addNeighbor(node: Node): Neighborhood

    /**
     * Remove a node neighbor.
     * @param node the node to remove
     * @return a new neighborhood without the provided node.
     */
    fun removeNeighbor(node: Node): Neighborhood
}
