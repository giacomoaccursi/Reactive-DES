/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

import entity.DoubleTime
import entity.EventImpl
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestListScheduler : StringSpec({
    "test scheduler with no events" {
        val emptyScheduler = ListScheduler()
        emptyScheduler.getNext() shouldBe null
    }

    "test scheduler with one event" {
        val event = EventImpl()
        val scheduler = ListScheduler(ArrayList(listOf(event)))
        scheduler.getNext() shouldBe event
    }

    "it must be possible to remove events from the scheduler" {
        val event = EventImpl()
        val emptyScheduler = ListScheduler(ArrayList(listOf(event)))
        emptyScheduler.removeEvent(event)
        emptyScheduler.getNext() shouldBe null
    }

    "scheduler with more than one event must return the event with smaller execution time" {
        val currentTime = DoubleTime()
        val event1 = EventImpl().also { it.initializationComplete(currentTime) }
        val event2 = EventImpl().also { it.initializationComplete(currentTime) }
        val scheduler = ListScheduler(ArrayList(listOf(event1, event2)))
        val smaller = listOf(event1, event2).sortedBy { it.tau.toDouble() }.first()
        scheduler.getNext() shouldBe smaller
    }

    "scheduler must reorder the events execution after them update" {
        val currentTime = DoubleTime()
        val event1 = EventImpl().also { it.initializationComplete(currentTime) }
        val event2 = EventImpl().also { it.initializationComplete(currentTime) }
        val event3 = EventImpl().also { it.initializationComplete(currentTime) }
        var inverseSortedEventList =
            listOf(event1, event2, event3)
        inverseSortedEventList = inverseSortedEventList.sortedByDescending { event -> event.tau.toDouble() }
        val scheduler = ListScheduler(ArrayList(inverseSortedEventList))
        scheduler.eventsUpdated()
        scheduler.events shouldBe inverseSortedEventList.sortedBy { it.tau.toDouble() }
    }
})
