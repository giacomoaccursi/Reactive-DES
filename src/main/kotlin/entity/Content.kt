/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.AwaitableMutableStateFlow

/**
 * A container of values.
 */
interface Content {
    /**
     * The flow of the value.
     */
    var value: AwaitableMutableStateFlow<Int>

    /**
     * Sets the value with the provided one.
     * @param value the new value.
     */
    suspend fun setValue(value: Int)
}
