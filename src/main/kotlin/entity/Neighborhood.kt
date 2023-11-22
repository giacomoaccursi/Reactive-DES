/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.CoroutineScope

/**
 * Interface for a neighborhood.
 */
interface Neighborhood {

    /**
     * Starts to observing environment nodes.
     * @param scope the coroutine scope.
     */
    suspend fun startObservingNodes(scope: CoroutineScope)

    /**
     * @return the center of the neighborhood.
     */
    fun getCenter(): Node

    /**
     * @return the neighbors of the neighborhood.
     */
    fun getNeighbors(): Set<Node>

    /**
     * Add new neighbor.
     * @param node the node to add.
     */
    fun addNeighbor(node: Node)

    /**
     * Remove a node from neighbors.
     * @param node the node to remove.
     */
    fun removeNeighbor(node: Node)
}
