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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A simple implementation of a neighborhood.
 */
class SimpleNeighborhood(
    override val center: Node,
    private val environment: Environment,
    private val linkingRule: LinkingRule,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
) : Neighborhood {

    private val neighborsSet: MutableStateFlow<Set<Node>> = MutableStateFlow(emptySet())
    override val neighbors: StateFlow<Set<Node>>
        get() = neighborsSet.asStateFlow()

    init {
        startObservingNodes()
    }

    private fun startObservingNodes() {
        CoroutineScope(coroutineContext).launch {
            environment.nodesToPosition.collect {
                neighborsSet.value = linkingRule.computeNeighbors(center, environment)
            }
        }
    }
}
