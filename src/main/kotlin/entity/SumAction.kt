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
 * Simple implementation of an action. It adds one to the value of all contents of a random node.
 */
class SumAction(private val environment: Environment) : Action {
    override suspend fun execute() {
        val node = environment.nodes[Random.nextInt(environment.nodes.size)]
        node.contents.forEach {
            it.value += 1
        }
    }
}
