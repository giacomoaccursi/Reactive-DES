/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

import entity.Environment
import entity.Time
import kotlinx.coroutines.delay
import java.util.concurrent.CountDownLatch

/**
 * Implementation of the engine.
 */
class EngineImpl(
    private val environment: Environment,
    private val scheduler: Scheduler,
    private val maxSteps: Int,
    time: Time,
) : Engine {

    private var currentStep: Int = 0
    private var currentTime = time
    private var status = Status.INIT
    private lateinit var latch: CountDownLatch

    override suspend fun start() {
        scheduleEvents()
        delay(1000)
        status = Status.RUNNING
        while (currentStep < maxSteps && status == Status.RUNNING) {
            doStep()
            environment.getAllNodes().forEach {
                println(
                    "node ${it.id} in position ${environment.getNodePosition(it)}, neighbors = ${
                        environment.getNeighborhood(
                            it,
                        )?.neighbors?.map { n -> n.id }
                    }",
                )
            }
        }
        status = Status.TERMINATED
    }

    override fun notifyEventUpdate() {
        this.latch.countDown()
    }

    private fun scheduleEvents() {
        this.latch = CountDownLatch(environment.getAllNodes().size)
        environment.getAllNodes().forEach { node ->
            node.events.value.forEach { event ->
                event.initializationComplete(currentTime)
                scheduler.addEvent(event)
            }
        }
    }

    private suspend fun doStep() {
        println("***************************")
        println("start step $currentStep")
        val nextEvent = scheduler.getNext()
        if (nextEvent == null) {
            status = Status.TERMINATED
        } else {
            val scheduledTime = nextEvent.tau
            if (scheduledTime.toDouble() < currentTime.toDouble()) {
                error("next event is scheduled in the past")
            }
            currentTime = scheduledTime
            if (nextEvent.canExecute()) {
                this.latch = CountDownLatch(nextEvent.getNumberOfEventExecutionObserver())
                nextEvent.execute()
                waitForEventUpdate()
                nextEvent.updateEvent(currentTime)
                scheduler.eventsUpdated()
            }
        }
        println("end step $currentStep")
        currentStep += 1
    }

    private fun waitForEventUpdate() {
        latch.await()
    }

    /**
     * Enum for the status of the simulation.
     */
    enum class Status {
        /**
         * The simulation is being initialized.
         */
        INIT,

        /**
         * The simulation is running.
         */
        RUNNING,

        /**
         * The simulation is terminated.
         */
        TERMINATED,
    }
}
