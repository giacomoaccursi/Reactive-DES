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
        while (currentStep < maxSteps && status == Status.RUNNING) {
            doStep()
        }
        status = Status.TERMINATED
    }

    private fun scheduleEvents() {
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
                nextEvent.execute()
                nextEvent.updateEvent(currentTime)
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
