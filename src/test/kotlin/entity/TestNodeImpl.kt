/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import control.EngineImpl
import control.ListScheduler
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class TestNodeImpl : StringSpec({
    val environment = EnvironmentImpl(PositionLinkingRule(2.0))
    val engine = EngineImpl(environment, ListScheduler(), 10, DoubleTime())

    "events are added corretly to the node" {
        val node = NodeImpl(1)
        node.addEvent(EventImpl(node, engine = engine, environment = environment))
        node.addEvent(EventImpl(node, engine = engine, environment = environment))
        node.events.value.size shouldBeExactly 2
    }

    "events are removed correctly from the node" {
        val node = NodeImpl(1)
        val event = EventImpl(node, engine = engine, environment = environment)
        node.addEvent(event)
        node.removeEvent(event)
        node.events.value.size shouldBeExactly 0
    }
})
