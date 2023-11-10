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
        val value = 1
        val node = NodeImpl(contents = ArrayList(listOf(ContentImpl(value), ContentImpl(value))))
        val action = SumAction(node)
        action.execute()
        node.contents.forEach {
            it.value shouldBeExactly value + 1
        }
    }
})
