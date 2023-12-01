/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Implementation of an event.
 */
class EventImpl(
    override val node: Node,
    private val conditions: ArrayList<Condition> = ArrayList(),
    private val actions: ArrayList<Action> = ArrayList(),
    override val timeDistribution: TimeDistribution,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
) : Event {

    private val observedEvents: HashMap<Event, Job> = hashMapOf()
    override val executionFlow: MutableSharedFlow<Event> = MutableSharedFlow()

    init {
        observeLocalEvents()
    }

    override val tau: Time get() = timeDistribution.getNextOccurrence()

    override suspend fun execute() {
        actions.forEach { it.execute() }
        executionFlow.emit(this)
    }

    override fun canExecute(): Boolean {
        return conditions.all { it.isValid() }
    }

    override fun initializationComplete(currentTime: Time) {
        updateEvent(currentTime)
    }

    override fun updateEvent(currentTime: Time) {
        timeDistribution.update(currentTime)
    }

    private suspend fun observeEvent(event: Event): Job {
        return CoroutineScope(coroutineContext).launch {
            event.executionFlow.collect {
                println("node: ${node.id} event $event has been executed")
            }
        }
    }

    private fun observeLocalEvents() {
        CoroutineScope(coroutineContext).launch {
            node.events.collect {
                val removed = observedEvents.keys - it.toSet() - setOf(this@EventImpl)
                val added = it.toSet() - setOf(this@EventImpl) - observedEvents.keys
                removed.forEach { event ->
                    println("removed event, stop to observe")
                    observedEvents[event]?.cancel()
                    observedEvents.remove(event)
                }
                added.forEach { event ->
                    println("added event, start to observe")
                    val job = observeEvent(event)
                    observedEvents[event] = job
                }
            }
        }
    }
}
