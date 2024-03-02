/*
 * Copyright (c) 2024. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

import entity.Environment
import entity.Time
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*

/**
 * A class to export simulation data in a yaml file.
 */
class SimulationExporter {
    /**
     * Export simulation data in yaml file.
     * @param environment the environment of the simulation.
     * @param currentTime the current time of the simulation.
     * @param currentStep the current step of the simulation.
     * @param scheduler the scheduler of the simulation.
     */
    fun exportData(environment: Environment, currentTime: Time, currentStep: Int, scheduler: Scheduler) {
        val options = DumperOptions()
        options.isPrettyFlow = true
        val yaml = Yaml(options)
        val resourceFolder = File("src/main/resources")
        val outputFile = File(resourceFolder, "step-$currentStep.yml").bufferedWriter()
        val time = mapOf("time" to String.format(Locale.getDefault(), "%.2f", currentTime.toDouble()).toDouble())
        val step = mapOf("step" to currentStep)
        val nodes = mapOf("nodes" to getEnvironmentNodes(environment).map { it.id })
        val neighborhoods = mapOf("neighborhoods" to getEnvironmentNeighborhood(environment))
        val eventsDependencies = mapOf(
            "events" to getEventDependencies(environment).map {
                mapOf(
                    "id" to it.key.id,
                    "node" to it.key.node.id,
                    "dependencies" to it.value.map { ev -> ev.id },
                )
            },
        )
        val futureEventList =
            mapOf(
                "event_list" to getFutureEventList(scheduler).map {
                    mapOf(
                        "id" to it.key,
                        "time" to String.format(Locale.getDefault(), "%.2f", it.value).toDouble(),
                    )
                },
            )
        getFutureEventList(scheduler)
        val yamlMap = time + step + nodes + neighborhoods + eventsDependencies + futureEventList
        yaml.dump(yamlMap, outputFile)
        outputFile.close()
    }

    private fun getEnvironmentNodes(environment: Environment) =
        environment.getAllNodes()

    private fun getEnvironmentNeighborhood(environment: Environment) =
        environment.neighborhoods.value.map {
            it.key to it.value.neighbors.map { node -> node.id }
        }.toMap()

    private fun getEventDependencies(environment: Environment) =
        environment.getAllNodes()
            .flatMap { node ->
                node.events.value
            }.associateWith { event -> event.getObservedEvents() }

    private fun getFutureEventList(scheduler: Scheduler) =
        scheduler.getEventList()
            .associate { event ->
                event.id to event.tau.toDouble()
            }
}
