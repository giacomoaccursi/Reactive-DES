/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import control.Engine
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
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val engine: Engine,
) : Event {

    private val timeDistribution: TimeDistribution = TimeDistribution(DoubleTime(2.0))
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

    override fun getNumberOfEventExecutionObserver() = executionFlow.subscriptionCount.value

    override fun updateEvent(currentTime: Time) {
        timeDistribution.update(currentTime)
        engine.notifyEventUpdate()
    }

    private suspend fun observeEvent(event: Event): Job {
        return CoroutineScope(coroutineContext).launch {
            event.executionFlow.collect {
                updateEvent(it.tau)
            }
        }
    }

    private fun observeLocalEvents() {
        CoroutineScope(coroutineContext).launch {
            node.events.collect {
                val removed = observedEvents.keys - it.toSet() - setOf(this@EventImpl)
                val added = it.toSet() - setOf(this@EventImpl) - observedEvents.keys
                removed.forEach { event ->
                    observedEvents[event]?.cancel()
                    observedEvents.remove(event)
                }
                added.forEach { event ->
                    val job = observeEvent(event)
                    observedEvents[event] = job
                }
            }
        }
    }
}
