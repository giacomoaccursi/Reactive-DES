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
import kotlin.coroutines.CoroutineContext

/**
 * A simple implementation of a neighborhood.
 */
class SimpleNeighborhood(
    private val center: Node,
    private val environment: Environment,
    private val linkingRule: LinkingRule,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
) : Neighborhood {

    init {
        startObservingNodes()
    }

    private var neighbors: Set<Node> = emptySet()

    private fun startObservingNodes() {
        CoroutineScope(coroutineContext).launch {
            environment.nodesToPosition.collect {
                neighbors = linkingRule.computeNeighbors(center, environment)
            }
        }
    }

    override fun getCenter() = center

    override fun getNeighbors() = neighbors
}
