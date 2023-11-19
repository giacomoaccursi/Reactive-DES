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
    override fun start() {
        scheduleEvents()
        while (currentStep < maxSteps) {
            doStep()
        }
    }

    private fun scheduleEvents() {
        // Occorre anche creare le dipendenze fra le reazioni in base al contesto.
        environment.nodes.forEach { node ->
            node.events.forEach { event ->
                event.initializationComplete(currentTime)
                scheduler.addEvent(event)
            }
        }
    }

    private fun doStep() {
        val nextEvent = scheduler.getNext()
        if (nextEvent == null) {
            // Simple way to block the loop, to improve.
            currentStep = maxSteps
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
        currentStep += 1
    }
}
