/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Implementation of time with double number.
 */
class DoubleTime(private val time: Double = 0.0) : Time {
    override fun plus(other: Time): Time {
        return DoubleTime(time + other.toDouble())
    }

    override fun toDouble(): Double {
        return time
    }
}
