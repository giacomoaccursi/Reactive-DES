/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * An implementation of node.
 */
class NodeImpl(
    override val id: Int,
) : Node {

    override val events: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    override val contents: MutableStateFlow<List<Content>> = MutableStateFlow(emptyList())

    override fun addEvent(event: Event) {
        events.value += event
    }

    override fun removeEvent(event: Event) {
        event.eventRemoved()
        events.value -= event
    }

    override fun addContent(content: Content) {
        contents.value += content
    }

    override fun removeContent(content: Content) {
        contents.value -= content
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeImpl

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}
