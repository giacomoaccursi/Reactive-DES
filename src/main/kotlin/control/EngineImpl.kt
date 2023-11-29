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

    override suspend fun start() {
        scheduleEvents()
        status = Status.RUNNING
        delay(1000)
        while (currentStep < maxSteps && status == Status.RUNNING) {
            doStep()
            delay(1000)
        }
        status = Status.TERMINATED

        environment.getAllNodes().forEach {
            it.contents.forEach { content ->
                println("node = ${it.id} content = ${content.value}")
            }
        }
        println("----------------------------------------")
        environment.getAllNodes().forEach {
            println("node ${it.id} position = ${environment.getNodePosition(it)}")
        }

        environment.neighborhoods.forEach {
            println("center = ${it.getCenter().id}, neighbors = ${it.getNeighbors().map { node -> node.id }}")
        }
    }

    private fun scheduleEvents() {
        // Occorre anche creare le dipendenze fra le reazioni in base al contesto.
        environment.getAllNodes().forEach { node ->
            node.events.forEach { event ->
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
                nextEvent.execute()
                // da capire bene come gestirlo: dopo aver eseguito l'evento, verranno aggiornati anche tutti
                // gli eventi dipendenti. Essendo questo aggiornamento asincrono non possiamo informare subito lo
                // scheduler che gli eventi sono stati aggiornati.
                scheduler.eventsUpdated()
            }
        }
        println("end step $currentStep")
        currentStep += 1
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
