/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 *  An interface that represent an auto-linking logic for nodes within an
 *  environment.
 */
interface LinkingRule {
    /**
     * Calculates neighbors for specified node considering its position.
     *
     * @param center
     *            the node to recompute.
     * @param environment
     *            the node's environment.
     * @return the set of neighbors.
     */
    fun computeNeighbors(center: Node, environment: Environment): Set<Node>

    /**
     * Check if a node is in the neighborhood of the center node.
     * @param center the center of a neighborhood.
     * @param other another node.
     * @param environment the environment.
     * @return true if other is a neighbor of the center node, false otherwise.
     */
    fun isNeighbor(center: Node, other: Node, environment: Environment): Boolean
}
