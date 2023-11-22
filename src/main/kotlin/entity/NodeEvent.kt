/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Interface for change events of a node.
 */
sealed interface NodeEvent {
    /**
     * New Node is added to the environment.
     * @param node the added node.
     */
    class NodeAdded(val node: Node) : NodeEvent

    /**
     * A node is removed from the environment.
     * @param node the removed node.
     */
    class NodeRemoved(val node: Node) : NodeEvent

    /**
     * A node is moved in the environment.
     * @param node the moved node.
     */
    class NodeMoved(val node: Node) : NodeEvent
}
