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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A simple implementation of a neighborhood.
 */
class SimpleNeighborhood(
    private val center: Node,
    private val environment: Environment,
    private val linkingRule: LinkingRule,
    private val nodeChangeFlow: MutableSharedFlow<NodeEvent>,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) : Neighborhood {

    init {
        CoroutineScope(coroutineContext).launch {
            startObservingNodes(this)
        }
    }

    private var neighbors: Set<Node> =
        linkingRule.computeNeighbors(center, environment)

    override suspend fun startObservingNodes(scope: CoroutineScope) {
        scope.launch {
            nodeChangeFlow.collect { event ->
                when (event) {
                    is NodeEvent.NodeAdded -> onNodeAdded(event.node)
                    is NodeEvent.NodeMoved -> onNodeMoved(event.node)
                    is NodeEvent.NodeRemoved -> onNodeRemoved(event.node)
                }
            }
        }
    }

    private fun onNodeMoved(node: Node) {
        if (node != center) {
            if (linkingRule.isNeighbor(center, node, environment)) {
                if (!neighbors.contains(node)) {
                    neighbors += node
                }
            } else {
                if (neighbors.contains(node)) {
                    neighbors -= node
                }
            }
        } else {
            neighbors = linkingRule.computeNeighbors(center, environment)
        }
    }

    private fun onNodeAdded(node: Node) {
        if (node != center) {
            if (linkingRule.isNeighbor(center, node, environment)) {
                if (!neighbors.contains(node)) {
                    neighbors += node
                }
            }
        }
    }

    private fun onNodeRemoved(node: Node) {
        if (neighbors.contains(node)) {
            neighbors -= node
        }
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
