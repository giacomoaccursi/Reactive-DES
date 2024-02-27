/*
 * Copyright (c) 2024. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

/**
 * Base implementation of a custom flow that waits for notification of consumed element.
 */
open class AwaitableMutableFlow<T>(
    private val flow: MutableSharedFlow<T>,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO,
) {
    private var emitLatch: CountDownLatch = CountDownLatch(0)

    /**
     * Perform emit waiting for notification.
     */
    suspend fun emitAndWait(value: T) {
        emitLatch = CountDownLatch(flow.subscriptionCount.value)
        flow.emit(value)
        withContext(ioDispatcher) {
            emitLatch.await()
        }
        emitLatch = CountDownLatch(0)
    }

    /**
     * A function for notifying the consumption.
     */
    fun notifyConsumed() {
        emitLatch.countDown()
    }
}
