/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

/**
 * A simple implementation of a neighborhood.
 */
class SimpleNeighborhood(
    private val center: Node,
    private val environment: Environment,
    private val linkingRule: LinkingRule,
) : Neighborhood {

    private var neighbors: Set<Node> = setOf()
    override fun startObservingNodes(scope: CoroutineScope): CountDownLatch {
        val latch = CountDownLatch(environment.nodes.size)
        environment.nodes.forEach { node ->
            latch.countDown()
            scope.launch {
                node.observeNodePosition().collect {
                    if (node != center) {
                        if (linkingRule.isNeighbor(center, node)) {
                            neighbors += node
                        } else {
                            neighbors -= node
                        }
                    }
                }
            }
        }
        return latch
    }

    override fun getCenter() = center
    override fun getNeighbors() = neighbors

    override fun addNeighbor(node: Node) {
        neighbors += node
    }

    override fun removeNeighbor(node: Node) {
        neighbors -= node
    }
}
