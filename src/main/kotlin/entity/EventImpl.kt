/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.AwaitableMutableSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
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
    override val id: Int,
) : Event {

    private val timeDistribution: TimeDistribution = TimeDistribution(DoubleTime(2.0))
    private val observedLocalEvents = ConcurrentHashMap<Event, Job>()
    private val observedNeighborEvents = ConcurrentHashMap<Node, HashMap<Event, Job>>()
    private val observedNeighbors = ConcurrentHashMap<Node, Job>()
    private val executionFlow: AwaitableMutableSharedFlow<Event> = AwaitableMutableSharedFlow(MutableSharedFlow())

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
    override suspend fun eventRemoved() {
        observedLocalEvents.values.forEach {
            it.cancelAndJoin()
        }
        observedNeighborEvents.values.forEach {
            it.values.forEach { job -> job.cancelAndJoin() }
        }
    }

    override fun observeExecution(): AwaitableMutableSharedFlow<Event> = executionFlow

    override fun getObservedEvents(): List<Event> {
        return (observedLocalEvents.keys().toList() + observedNeighborEvents.values.flatMap { it.keys })
    }

    override fun updateEvent(currentTime: Time) {
        timeDistribution.update(currentTime)
    }

    private fun observeLocalEvents() {
        coroutineScope.launch {
            node.events.run {
                this.onSubscription {
                    initLatch.countDown()
                }.collect {
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
                }.collect { neighbors ->
                    val removedNeighbors = observedNeighbors.keys - neighbors
                    val addedNeighbors = neighbors - observedNeighbors.keys
                    removedNeighbors.forEach { node ->
                        observedNeighbors[node]?.cancelAndJoin()
                        observedNeighbors.remove(node)
                        observedNeighborEvents[node]?.values?.forEach {
                            it.cancelAndJoin()
                        }
                        observedNeighborEvents.remove(node)
                    }
                    addedNeighbors.forEach { node ->
                        val job = launch {
                            node.events.run {
                                this.collect { events ->
                                    val added =
                                        events.toSet() - observedNeighborEvents[node]?.keys.orEmpty()
                                    observedNeighborEvents[node]?.keys?.minus(events.map { it.id }.toSet())
                                        ?.forEach { event ->
                                            observedNeighborEvents[node]?.get(event)?.cancelAndJoin()
                                            observedNeighborEvents.remove(event)
                                        }
                                    added.forEach { event ->
                                        val job = launch {
                                            val executionFlow = event.observeExecution()
                                            executionFlow.run {
                                                this.collect { ev ->
                                                    updateEvent(ev.tau)
                                                    this.notifyConsumed()
                                                }
                                            }
                                        }
                                        this@EventImpl.observedNeighborEvents[node]?.set(event, job)
                                    }
                                    this.notifyConsumed()
                                }
                            }
                        }
                        this@EventImpl.observedNeighbors[node] = job
                        this@EventImpl.observedNeighborEvents[node] = hashMapOf()
                    }
                    this.notifyConsumed()
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventImpl
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
