/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.CustomMutableSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

/**
 * Implementation of an event.
 */
class EventImpl(
    override val node: Node,
    private val conditions: ArrayList<Condition> = ArrayList(),
    private val actions: ArrayList<Action> = ArrayList(),
    private val environment: Environment,
    coroutineContext: CoroutineContext = Dispatchers.Default,
) : Event {

    private val timeDistribution: TimeDistribution = TimeDistribution(DoubleTime(2.0))
    private val observedLocalEvents: HashMap<Event, Job> = hashMapOf()
    private val observedNeighborEvents: HashMap<Event, Job> = hashMapOf()
    private val executionFlow: CustomMutableSharedFlow<Event> = CustomMutableSharedFlow(MutableSharedFlow())

    private val coroutineScope = CoroutineScope(coroutineContext)
    private var initLatch: CountDownLatch

    init {
        val observationFunctions = listOf(::observeLocalEvents, ::observeNeighborEvents)
        initLatch = CountDownLatch(observationFunctions.size)
        observationFunctions.forEach {
            it.invoke()
        }
        initLatch.await()
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
        observedNeighborEvents.values.forEach {
            it.cancel()
        }
    }

    override fun observeExecution(): CustomMutableSharedFlow<Event> = executionFlow

    override fun updateEvent(currentTime: Time) {
        timeDistribution.update(currentTime)
    }

    private fun observeLocalEvents() {
        coroutineScope.launch {
            node.events.run {
                this.onSubscription { initLatch.countDown() }.collect {
                    val removed = observedLocalEvents.keys - it.toSet() - setOf(this@EventImpl)
                    val added = it.toSet() - setOf(this@EventImpl) - observedLocalEvents.keys
                    removed.forEach { event ->
                        observedLocalEvents[event]?.cancelAndJoin()
                        observedLocalEvents.remove(event)
                    }
                    added.forEach { event ->
                        val job = launch {
                            val executionFlow = event.observeExecution()
                            executionFlow.run {
                                this.collect { event ->
                                    updateEvent(event.tau)
                                    this.notifyConsumed()
                                }
                            }
                        }
                        observedLocalEvents[event] = job
                    }
                    this.notifyConsumed()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNeighborEvents() {
        coroutineScope.launch {
            environment.neighborhoods.run {
                this.onSubscription {
                    initLatch.countDown()
                }.mapLatest {
                    it[node.id]?.neighbors.orEmpty()
                }.mapLatest {
                    it.flatMap { node ->
                        node.events.value
                    }
                }.collect { events ->
                    val removed = observedNeighborEvents.keys - events.toSet()
                    val added = events.toSet() - observedNeighborEvents.keys
                    removed.forEach { event ->
                        observedNeighborEvents[event]?.cancelAndJoin()
                        observedNeighborEvents.remove(event)
                    }
                    added.forEach { event ->
                        val job = launch {
                            val executionFlow = event.observeExecution()
                            executionFlow.run {
                                this.collect { event ->
                                    updateEvent(event.tau)
                                    this.notifyConsumed()
                                }
                            }
                        }
                        observedNeighborEvents[event] = job
                    }
                    this.notifyConsumed()
                }
            }
        }
    }
}
