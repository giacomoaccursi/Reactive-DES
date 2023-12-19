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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.mapLatest
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
    private val environment: Environment,
) : Event {

    private val timeDistribution: TimeDistribution = TimeDistribution(DoubleTime(2.0))
    private val observedLocalEvents: HashMap<Event, Job> = hashMapOf()
    private val observedNeighborEvents: HashMap<Event, Job> = hashMapOf()
    private val executionFlow: MutableSharedFlow<Event> = MutableSharedFlow()

    init {
        observeLocalEvents()
        observeNeighborEvents()
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
    override fun eventRemoved() {
        observedLocalEvents.values.forEach {
            it.cancel()
        }
    }

    override fun observeExecution(): Flow<Event> = executionFlow.asSharedFlow()

    override fun updateEvent(currentTime: Time) {
        timeDistribution.update(currentTime)
        engine.notifyEventUpdate()
    }

    private fun observeLocalEvents() {
        CoroutineScope(coroutineContext).launch {
            node.events.collect {
                val removed = observedLocalEvents.keys - it.toSet() - setOf(this@EventImpl)
                val added = it.toSet() - setOf(this@EventImpl) - observedLocalEvents.keys
                removed.forEach { event ->
                    observedLocalEvents[event]?.cancel()
                    observedLocalEvents.remove(event)
                }
                added.forEach { event ->
                    val job = launch {
                        event.observeExecution().collect { event ->
                            updateEvent(event.tau)
                        }
                    }
                    observedLocalEvents[event] = job
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNeighborEvents() {
        CoroutineScope(coroutineContext).launch {
            environment.neighbors(node).mapLatest {
                it.flatMap { node ->
                    node.events.value
                }
            }.collect { events ->
                val removed = observedNeighborEvents.keys - events.toSet()
                val added = events.toSet() - observedNeighborEvents.keys
                removed.forEach { event ->
                    observedNeighborEvents[event]?.cancel()
                    observedNeighborEvents.remove(event)
                }
                added.forEach { event ->
                    val job = launch {
                        event.observeExecution().collect { event ->
                            updateEvent(event.tau)
                        }
                    }
                    observedNeighborEvents[event] = job
                }
            }
        }
    }
}
