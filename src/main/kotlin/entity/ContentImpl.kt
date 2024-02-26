/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.AwaitableMutableStateFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Simple implementation of a content.
 */
class ContentImpl(initialValue: Int) : Content {

    override var value: AwaitableMutableStateFlow<Int> = AwaitableMutableStateFlow(MutableStateFlow(initialValue))
    override suspend fun setValue(value: Int) {
        this.value.emit(value)
    }
}
