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
     * @return the center of the neighborhood.
     */
    fun getCenter(): Node

    /**
     * @return the neighbors of the neighborhood.
     */
    fun getNeighbors(): StateFlow<Set<Node>>
}
