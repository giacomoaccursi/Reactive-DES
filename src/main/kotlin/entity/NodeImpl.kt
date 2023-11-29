/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * An implementation of node.
 */
class NodeImpl(
    override val id: Int,
    override val events: ArrayList<Event> = ArrayList(),
    override val contents: ArrayList<Content> = ArrayList(),
) : Node {

    override fun addEvent(event: Event) {
        events.add(event)
    }

    override fun removeEvent(event: Event) {
        events.remove(event)
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
