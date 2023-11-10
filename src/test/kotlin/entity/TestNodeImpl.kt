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
        val node = NodeImpl()
        node.addEvent(EventImpl())
        node.addEvent(EventImpl())
        node.events.size shouldBeExactly 2
    }

    "events are removed correctly from the node" {
        val event = EventImpl()
        val node = NodeImpl(events = ArrayList(listOf(event)))
        node.removeEvent(event)
        node.events.size shouldBeExactly 0
    }
})
