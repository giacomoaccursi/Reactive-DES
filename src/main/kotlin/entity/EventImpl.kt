/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Implementation of an event.
 */
class EventImpl(
    override val node: Node,
    private val conditions: ArrayList<Condition> = ArrayList(),
    private val actions: ArrayList<Action> = ArrayList(),
    override val timeEquation: TimeEquation,
) : Event {

    override val tau: Time get() = timeEquation.getNextOccurrence()

    override suspend fun execute() {
        actions.forEach { it.execute() }
    }

    override fun canExecute(): Boolean {
        return conditions.all { it.isValid() }
    }

    override fun initializationComplete(currentTime: Time) {
        updateEvent(currentTime)
    }

    override fun updateEvent(currentTime: Time) {
        timeEquation.update(currentTime)
    }
}
