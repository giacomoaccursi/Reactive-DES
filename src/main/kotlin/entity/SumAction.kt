/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Simple implementation of an action. It adds one to the value of all contents in the node.
 */
class SumAction(private val node: Node) : Action {
    override fun execute() {
        node.contents.forEach {
            it.value += 1
        }
    }
}
