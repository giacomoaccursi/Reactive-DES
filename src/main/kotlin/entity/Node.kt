/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Represent a node in the environment.
 */
interface Node {
    /**
     * List of all events inside the node.
     */
    val events: List<Event>

    /**
     * List of all contents inside the node.
     */
    val contents: List<Content>

    /**
     * Adds the event to the node.
     * @param event the event to add.
     */
    fun addEvent(event: Event)

    /**
     * Remove the event from the node.
     * @param event the event to remove.
     */
    fun removeEvent(event: Event)
}
