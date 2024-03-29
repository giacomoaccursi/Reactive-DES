/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import kotlin.random.Random

/**
 * An action that move a random node from the environment and adds 1 to the x and y coordinates.
 */
class MoveNodeAction(private val environment: Environment) : Action {
    override suspend fun execute() {
        val node = environment.getAllNodes()[Random.nextInt(environment.getAllNodes().size)]
        environment.moveNode(node, environment.getNodePosition(node).sum(Position(1.0, 1.0)))
    }
}
