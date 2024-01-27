/*
 * Copyright (c) 2024. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

/**
 * A custom mutableStateFlow waiting for the consumption of an emitted element.
 */
class CustomMutableStateFlow<T>(
    private val stateFlow: MutableStateFlow<T>,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
) : CustomMutableFlow<T>(stateFlow, ioDispatcher), MutableStateFlow<T> by stateFlow {

    override suspend fun emit(value: T) {
        this.performEmit(value)
    }

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        stateFlow.collect(collector)
    }

    override val replayCache: List<T>
        get() = stateFlow.replayCache

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        stateFlow.resetReplayCache()
    }

    override val subscriptionCount: StateFlow<Int>
        get() = stateFlow.subscriptionCount

    override fun tryEmit(value: T): Boolean {
        return stateFlow.tryEmit(value)
    }
}
