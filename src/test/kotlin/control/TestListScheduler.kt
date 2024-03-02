/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

import entity.DoubleTime
import entity.EnvironmentImpl
import entity.EventImpl
import entity.NodeImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestListScheduler : StringSpec({
    val node = NodeImpl(1)
    val environment = EnvironmentImpl()

    "test scheduler with no events" {
        val emptyScheduler = ListScheduler()
        emptyScheduler.getNext() shouldBe null
    }

    "test scheduler with one event" {
        val scheduler = ListScheduler()
        val event = EventImpl(node, environment = environment, id = 1)
        scheduler.addEvent(event)
        scheduler.getNext() shouldBe event
    }

    "it must be possible to remove events from the scheduler" {
        val emptyScheduler = ListScheduler()
        val event = EventImpl(node, environment = environment, id = 1)
        emptyScheduler.addEvent(event)
        emptyScheduler.removeEvent(event)
        emptyScheduler.getNext() shouldBe null
    }

    "scheduler with more than one event must return the event with smaller execution time" {
        val currentTime = DoubleTime()
        val scheduler = ListScheduler()
        val event1 =
            EventImpl(node, environment = environment, id = 1).also { it.initializationComplete(currentTime) }
        val event2 =
            EventImpl(node, environment = environment, id = 2).also { it.initializationComplete(currentTime) }
        scheduler.addEvent(event1)
        scheduler.addEvent(event2)
        val smaller = listOf(event1, event2).sortedBy { it.tau.toDouble() }.first()
        scheduler.getNext() shouldBe smaller
    }
})
