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
        val environment = EnvironmentImpl()
        val value = 1
        val node = NodeImpl(1)
        node.addContent(ContentImpl(value))
        node.addContent(ContentImpl(value))
        environment.addNode(node, Position(0.0, 0.0))
        val action = SumAction(environment)
        action.execute()
        node.contents.value.forEach {
            it.value.value shouldBeExactly value + 1
        }
    }
})
