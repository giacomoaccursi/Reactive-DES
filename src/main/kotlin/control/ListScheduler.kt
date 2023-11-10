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
 * Simple implementation of a scheduler using an ArrayList.
 */
class ListScheduler(
    /**
     * A list of all events.
     */
    val events: ArrayList<Event> = ArrayList(),
) : Scheduler {
    init {
        sortListByTau()
    }
    override fun addEvent(event: Event) {
        events.add(event)
        this.sortListByTau()
    }

    override fun getNext(): Event? {
        return events.firstOrNull()
    }

    override fun removeEvent(event: Event) {
        events.remove(event)
    }

    override fun eventsUpdated() {
        this.sortListByTau()
    }

    private fun sortListByTau() {
        events.sortBy { it.tau.toDouble() }
    }
}
