/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import flow.CustomMutableStateFlow

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
    val events: CustomMutableStateFlow<List<Event>>

    /**
     * List of all contents inside the node.
     */
    val contents: CustomMutableStateFlow<List<Content>>

    /**
     * Adds the event to the node.
     * @param event the event to add.
     */
    suspend fun addEvent(event: Event)

    /**
     * Remove the event from the node.
     * @param event the event to remove.
     */
    suspend fun removeEvent(event: Event)

    /**
     * Add content to the node.
     * @param content the content to add.
     */
    suspend fun addContent(content: Content)

    /**
     * Remove content from the node.
     * @param content the content to remove.
     */
    suspend fun removeContent(content: Content)
}
