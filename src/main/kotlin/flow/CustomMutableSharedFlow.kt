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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

/**
 * A custom mutableSharedFlow waiting for the consumption of an emitted element.
 */
class CustomMutableSharedFlow<T>(
    private val sharedFlow: MutableSharedFlow<T>,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
) : CustomMutableFlow<T>(sharedFlow, ioDispatcher), MutableSharedFlow<T> by sharedFlow {

    override suspend fun emit(value: T) {
        this.performEmit(value)
    }

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        sharedFlow.collect(collector)
    }

    override val replayCache: List<T>
        get() = sharedFlow.replayCache

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        sharedFlow.resetReplayCache()
    }

    override val subscriptionCount: StateFlow<Int>
        get() = sharedFlow.subscriptionCount

    override fun tryEmit(value: T): Boolean {
        return sharedFlow.tryEmit(value)
    }
}
