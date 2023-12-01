/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import org.apache.commons.math3.distribution.ExponentialDistribution

/**
 * Represent a time equation.
 */
class TimeDistribution(time: Time) {
    /**
     * Only for test purposes it has been initialized as exponential distribution.
     */
    private val distribution = ExponentialDistribution(1.0)
    private var tau: Time = time

    /**
     * Update the time equation.
     * @param currentTime the current time of the environment.
     */
    fun update(currentTime: Time) {
        val step = distribution.sample()
        tau = DoubleTime(currentTime.toDouble() + step)
    }

    /**
     * The next time at which this event will occur.
     */
    fun getNextOccurrence(): Time = tau
}
