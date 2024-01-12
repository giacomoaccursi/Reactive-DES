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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch

/**
 * A custom mutableSharedFlow waiting for the consumption of an emitted element.
 */
class CustomMutableSharedFlow<T>(private val flow: MutableSharedFlow<T>) : MutableSharedFlow<T> by flow {

    private lateinit var latch: CountDownLatch

    override suspend fun emit(value: T) {
        latch = CountDownLatch(subscriptionCount.value)
        flow.emit(value)
        withContext(Dispatchers.IO) {
            latch.await()
        }
        latch = CountDownLatch(0)
    }

    /**
     * A function for notifying the consumption.
     */
    fun notifyConsumed() {
        latch.countDown()
    }
}

/**
 * A custom mutableStateFlow waiting for the consumption of an emitted element.
 */
class CustomMutableStateFlow<T>(private val flow: MutableStateFlow<T>) : MutableStateFlow<T> by flow {
    private lateinit var latch: CountDownLatch

    override suspend fun emit(value: T) {
        latch = CountDownLatch(subscriptionCount.value)
        flow.emit(value)
        withContext(Dispatchers.IO) {
            latch.await()
        }
        latch = CountDownLatch(0)
    }

    /**
     * A function for notifying the consumption.
     */
    fun notifyConsumed() {
        latch.countDown()
    }
}
