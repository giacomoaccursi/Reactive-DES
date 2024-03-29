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
    override val center: Node,
    override val neighbors: Set<Node>,
) : Neighborhood {
    override fun addNeighbor(node: Node) = SimpleNeighborhood(center, neighbors + node)

    override fun removeNeighbor(node: Node) = SimpleNeighborhood(center, neighbors - node)
}
