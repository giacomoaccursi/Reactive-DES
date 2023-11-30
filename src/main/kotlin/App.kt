/*
 * Copyright (c) 2023. Giacomo Accursi
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

import control.EngineImpl
import control.ListScheduler
import entity.ContentImpl
import entity.DoubleTime
import entity.DummyCondition
import entity.EnvironmentImpl
import entity.EventImpl
import entity.MoveNodeAction
import entity.NodeImpl
import entity.Position
import entity.PositionLinkingRule
import entity.SumAction
import entity.TimeEquation
import kotlinx.coroutines.runBlocking

/**
 * Max num of steps in simulation.
 */
const val MAX_STEPS = 10

/**
 * Main Function.
 */
fun main() {
    runBlocking {
        // Simple initialization of a simulation.
        val time = DoubleTime(0.0)
        val timeEquation = TimeEquation(time)
        val condition = DummyCondition()
        val linkingRule = PositionLinkingRule(4.0)

        val environment = EnvironmentImpl(linkingRule)
        val node1 = NodeImpl(
            1,
            contents = arrayListOf(ContentImpl(1)),
        )
        val node2 = NodeImpl(
            2,
            contents = arrayListOf(ContentImpl(1)),
        )
        val node3 = NodeImpl(
            3,
            contents = arrayListOf(ContentImpl(1)),
        )
        val node4 = NodeImpl(
            4,
            contents = arrayListOf(ContentImpl(1)),
        )
        listOf(node1, node2, node3, node4).forEach { node ->
            environment.addNode(
                node,
                Position(0.0, 0.0),
            )
        }
        environment.neighborhoods.forEach {
            println("node: ${it.getCenter().id}, neighbors = ${it.getNeighbors().map { node -> node.id }}")
        }
        val moveAction = MoveNodeAction(environment)
        val sumAction = SumAction(environment)
        val event1 = EventImpl(
            node1,
            arrayListOf(condition),
            arrayListOf(moveAction, sumAction),
            timeEquation,
        ).also { node1.addEvent(it) }
        val event2 = EventImpl(
            node2,
            arrayListOf(condition),
            arrayListOf(sumAction),
            timeEquation,
        ).also { node2.addEvent(it) }
        val event3 = EventImpl(
            node3,
            arrayListOf(condition),
            arrayListOf(sumAction),
            timeEquation,
        ).also { node3.addEvent(it) }
        val event4 = EventImpl(
            node4,
            arrayListOf(condition),
            arrayListOf(sumAction),
            timeEquation,
        ).also { node4.addEvent(it) }
        val scheduler = ListScheduler(arrayListOf(event1, event2, event3, event4))
        val engine = EngineImpl(environment, scheduler, MAX_STEPS, time)
        engine.start()
    }
}
