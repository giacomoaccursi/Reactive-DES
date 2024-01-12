/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A linking rule based on positions.
 * @param radius the radius in which two position are neighbors.
 */
class PositionLinkingRule(private val radius: Double, private val environment: Environment) : LinkingRule {

    init {
        observeNodes()
    }

    override fun computeNeighbors(center: Node, environment: Environment) =
        environment.getAllNodes().filter {
            isNeighbor(center, it, environment)
        }.toSet().minus(center)

    override fun isNeighbor(center: Node, other: Node, environment: Environment): Boolean {
        return environment.getNodePosition(center).distanceTo(environment.getNodePosition(other)) <= radius
    }

    private fun observeNodes() {
        CoroutineScope(Dispatchers.Default).launch {
            environment.nodesToPosition.collect {
                val newNeighborhoods = environment.getAllNodes().associate { node ->
                    node.id to SimpleNeighborhood(node, computeNeighbors(node, environment))
                }
                environment.updateNeighborhoods(newNeighborhoods)
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            environment.nodes.collect {
                val newNeighborhoods = environment.getAllNodes().associate { node ->
                    node.id to SimpleNeighborhood(node, computeNeighbors(node, environment))
                }
                environment.updateNeighborhoods(newNeighborhoods)
            }
        }
    }
}
