/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * A linking rule based on positions.
 * @param radius the radius in which two position are neighbors.
 */
class PositionLinkingRule(private val radius: Double) : LinkingRule {
    override fun computeNeighbors(center: Node, environment: Environment) =
        environment.nodes.filter {
            isNeighbor(center, it)
        }.toSet()

    override fun isNeighbor(center: Node, other: Node): Boolean {
        return center.getPosition().distanceTo(other.getPosition()) <= radius
    }
}
