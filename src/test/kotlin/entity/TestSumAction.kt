/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class TestSumAction : StringSpec({
    "sum action must add 1 to the value of all content in the node" {
        val radius = 5.0
        val linkingRule = PositionLinkingRule(radius)
        val environment = EnvironmentImpl(linkingRule = linkingRule)
        val value = 1
        val node = NodeImpl(
            1,
            contents = ArrayList(listOf(ContentImpl(value), ContentImpl(value))),
            initialPosition = Position(0.0, 0.0),
        )
        environment.addNode(node)
        val action = SumAction(environment)
        action.execute()
        node.contents.forEach {
            it.value shouldBeExactly value + 1
        }
    }
})
