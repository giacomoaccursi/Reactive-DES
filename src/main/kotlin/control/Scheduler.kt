/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

import entity.Event

/**
 * Interface of a scheduler of events.
 */
interface Scheduler {
    /**
     * Adds an event to the scheduler.
     * @param event the event to add.
     */
    fun addEvent(event: Event)

    /**
     * Remove an event from the scheduler.
     * @param event the event to remove.
     */
    fun removeEvent(event: Event)

    /**
     * @return the next event to process, if present.
     */
    fun getNext(): Event?

    /**
     * Must be called when at least one event updated its time.
     */
    fun eventsUpdated()
}
