/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class TestNodeImpl : StringSpec({
    "events are added corretly to the node" {
        val timeEquation = TimeEquation(DoubleTime(0.0))
        val node = NodeImpl(1)
        node.addEvent(EventImpl(node, timeEquation = timeEquation))
        node.addEvent(EventImpl(node, timeEquation = timeEquation))
        node.events.value.size shouldBeExactly 2
    }

    "events are removed correctly from the node" {
        val timeEquation = TimeEquation(DoubleTime(0.0))
        val node = NodeImpl(1)
        val event = EventImpl(node, timeEquation = timeEquation)
        node.addEvent(event)
        node.removeEvent(event)
        node.events.value.size shouldBeExactly 0
    }
})
