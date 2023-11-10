/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Represent a condition.
 */
interface Condition {
    /**
     * @return true if the condition is satisfied, false otherwise.
     */
    fun isValid(): Boolean
}
