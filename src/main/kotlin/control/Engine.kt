/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package control

/**
 * The engine of the simulation.
 */
interface Engine {
    /**
     * Start the engine.
     */
    suspend fun start()
}
