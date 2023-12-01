/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlinx.coroutines.flow.StateFlow

/**
 * Represent a node in the environment.
 */
interface Node {
    /**
     * The id of the node.
     */
    val id: Int

    /**
     * List of all events inside the node.
     */
    val events: StateFlow<List<Event>>

    /**
     * List of all contents inside the node.
     */
    val contents: StateFlow<List<Content>>

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

    /**
     * Add content to the node.
     * @param content the content to add.
     */
    fun addContent(content: Content)

    /**
     * Remove content from the node.
     * @param content the content to remove.
     */
    fun removeContent(content: Content)
}
