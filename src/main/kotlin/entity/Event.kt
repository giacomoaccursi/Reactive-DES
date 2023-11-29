/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Represent an event that can happen.
 */
interface Event {
    /**
     * Executes the event.
     */
    suspend fun execute()

    /**
     * Verifies if the event can be executed.
     * @return true if the event can be executed, false otherwise.
     */
    fun canExecute(): Boolean

    /**
     * Updates the scheduling of the event.
     * @param currentTime the current time of the simulation.
     */
    fun updateEvent(currentTime: Time)

    /**
     * It's called when the environment has completed its initialization.
     */
    fun initializationComplete(currentTime: Time)

    /**
     * The smallest context in which an event can read information.
     */
    val inputContext: Context

    /**
     * The smallest context in which an event can do modifications.
     */
    val outputContext: Context

    /**
     * The global time at which this reaction is scheduled to be executed.
     */
    val tau: Time

    /**
     * The time equation of this event.
     */
    val timeEquation: TimeEquation
}
