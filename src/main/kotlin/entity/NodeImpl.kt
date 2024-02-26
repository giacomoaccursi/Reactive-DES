/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.AwaitableMutableStateFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * An implementation of node.
 */
class NodeImpl(
    override val id: Int,
) : Node {

    override val events: AwaitableMutableStateFlow<List<Event>> =
        AwaitableMutableStateFlow(MutableStateFlow(emptyList()))
    override val contents: AwaitableMutableStateFlow<List<Content>> =
        AwaitableMutableStateFlow(MutableStateFlow(emptyList()))

    override suspend fun addEvent(event: Event) {
        events.emit(events.value + event)
    }

    override suspend fun removeEvent(event: Event) {
        events.emit(events.value - event)
    }

    override suspend fun addContent(content: Content) {
        contents.emit(contents.value + content)
    }

    override suspend fun removeContent(content: Content) {
        contents.emit(contents.value - content)
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
