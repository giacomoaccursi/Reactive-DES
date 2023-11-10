/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * The time in the simulation.
 */
interface Time {
    /**
     * Converts time to double.
     * @return the converted time.
     */
    fun toDouble(): Double

    /**
     * Allows to add time to this time.
     * @param other the time to sum to the current time.
     * @return the result of the sum.
     */
    operator fun plus(other: Time): Time
}
