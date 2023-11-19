/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * A simple implementation of a neighborhood.
 */
class SimpleNeighborhood(
    private val center: Node,
) : Neighborhood {

    private var neighbors: Set<Node> = setOf()

    override fun getCenter() = center
    override fun getNeighbors() = neighbors

    override fun addNeighbor(node: Node) {
        neighbors += node
    }

    override fun removeNeighbor(node: Node) {
        neighbors -= node
    }
}
