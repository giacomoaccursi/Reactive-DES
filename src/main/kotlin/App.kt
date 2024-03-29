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

/**
 * Max num of steps in simulation.
 */
const val MAX_STEPS = 1000

/**
 * Main Function.
 */
suspend fun main() {
    // Simple initialization of a simulation.
    val time = DoubleTime(0.0)
    val condition = DummyCondition()
    val environment = EnvironmentImpl()
    val linkingRule = PositionLinkingRule(1.0, environment = environment)
    environment.setLinkingRule(linkingRule)
    val node1 = NodeImpl(1)
    node1.addContent(ContentImpl(1))
    val node2 = NodeImpl(2)
    node2.addContent(ContentImpl(1))
    val node3 = NodeImpl(3)
    node3.addContent(ContentImpl(1))
    val node4 = NodeImpl(4)
    node4.addContent(ContentImpl(1))

    listOf(node1, node2, node3, node4).forEach { node ->
        environment.addNode(
            node,
            Position(0.0, 0.0),
        )
    }

    val moveAction = MoveNodeAction(environment)
    val sumAction = SumAction(environment)

    val scheduler = ListScheduler()
    val engine = EngineImpl(environment, scheduler, MAX_STEPS, time)

    val event1 = EventImpl(
        node1,
        arrayListOf(condition),
        arrayListOf(moveAction, sumAction),
        environment = environment,
        id = 1,
    )
    val event2 = EventImpl(
        node1,
        arrayListOf(condition),
        arrayListOf(moveAction, sumAction),
        environment = environment,
        id = 2,
    )
    node1.addEvent(event1)
    node1.addEvent(event2)

    val event3 = EventImpl(
        node2,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 3,
    )
    val event4 = EventImpl(
        node2,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 4,
    )
    node2.addEvent(event3)
    node2.addEvent(event4)

    val event5 = EventImpl(
        node3,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 5,
    )
    val event6 = EventImpl(
        node3,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 6,
    )
    node3.addEvent(event5)
    node3.addEvent(event6)

    val event7 = EventImpl(
        node4,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 7,
    )
    val event8 = EventImpl(
        node4,
        arrayListOf(condition),
        arrayListOf(moveAction),
        environment = environment,
        id = 8,
    )

    node4.addEvent(event7)
    node4.addEvent(event8)

    engine.start()
}
