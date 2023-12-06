/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.SharedFlow

/**
 * Represent an event that can happen.
 */
interface Event {
    /**
     * The node in which this event executes.
     */
    val node: Node

    /**
     * The global time at which this reaction is scheduled to be executed.
     */
    val tau: Time

    /**
     * Shared flow for event execution.
     */
    val executionFlow: SharedFlow<Event>

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
     * @return the number of the observers of this event.
     */
    fun getNumberOfEventExecutionObserver(): Int
}
