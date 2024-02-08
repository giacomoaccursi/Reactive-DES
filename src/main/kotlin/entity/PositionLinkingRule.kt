/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.CustomMutableFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

/**
 * A linking rule based on positions.
 * @param radius the radius in which two position are neighbors.
 */
class PositionLinkingRule(
    private val radius: Double,
    private val environment: Environment,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) : LinkingRule {
    private var initLatch: CountDownLatch
    private val coroutineScope = CoroutineScope(coroutineContext)

    init {
        val observationFunctions = listOf(::observeNodes, ::observeNodesPosition)
        initLatch = CountDownLatch(observationFunctions.size)
        observationFunctions.forEach {
            it.invoke()
        }
        initLatch.await()
    }

    override fun computeNeighbors(center: Node, environment: Environment) =
        environment.getAllNodes().filter {
            isNeighbor(center, it, environment)
        }.toSet().minus(center)

    override fun isNeighbor(center: Node, other: Node, environment: Environment): Boolean {
        return environment.getNodePosition(center).distanceTo(environment.getNodePosition(other)) <= radius
    }

    private fun observeNodes() {
        startToObserveFlow(environment.nodesToPosition)
    }

    private fun observeNodesPosition() {
        startToObserveFlow(environment.nodes)
    }

    private fun startToObserveFlow(flow: CustomMutableFlow<*>) {
        coroutineScope.launch {
            flow.onSubscription {
                initLatch.countDown()
            }.collect {
                val newNeighborhoods = environment.getAllNodes().associate { node ->
                    node.id to SimpleNeighborhood(node, computeNeighbors(node, environment))
                }
                environment.updateNeighborhoods(newNeighborhoods)
                flow.notifyConsumed()
            }
        }
    }
}
