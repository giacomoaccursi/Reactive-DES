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
 * Interface for a neighborhood.
 */
interface Neighborhood {

    /**
     * The center of the neighborhood.
     */
    val center: Node

    /**
     * The neighbors of the node.
     */
    val neighbors: StateFlow<Set<Node>>
}
