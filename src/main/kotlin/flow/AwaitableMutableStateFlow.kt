/*
 * Copyright (c) 2024. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package flow

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A custom mutableStateFlow waiting for the consumption of an emitted element.
 */
class AwaitableMutableStateFlow<T>(
    private val stateFlow: MutableStateFlow<T>,
) : AbstractAwaitableMutableFlow<T>(stateFlow), MutableStateFlow<T> by stateFlow {

    override suspend fun emit(value: T) {
        this.emitAndWait(value)
    }
}
