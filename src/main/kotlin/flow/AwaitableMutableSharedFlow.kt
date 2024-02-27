/*
 * Copyright (c) 2024. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package flow

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A custom mutableSharedFlow waiting for the consumption of an emitted element.
 */
class AwaitableMutableSharedFlow<T>(
    private val sharedFlow: MutableSharedFlow<T>,
) : AwaitableMutableFlow<T>(sharedFlow), MutableSharedFlow<T> by sharedFlow {

    override suspend fun emit(value: T) {
        this.emitAndWait(value)
    }
}
