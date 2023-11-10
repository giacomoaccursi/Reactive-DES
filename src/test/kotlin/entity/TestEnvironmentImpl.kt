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

class TestEnvironmentImpl : StringSpec({
    "nodes are added correctly to the environmemt" {
        val environment = EnvironmentImpl()
        environment.addNode(NodeImpl())
        environment.addNode(NodeImpl())
        environment.nodes.size shouldBeExactly 2
    }

    "nodes are removed correctly from the environmemt" {
        val environment = EnvironmentImpl()
        val node = NodeImpl()
        environment.addNode(node)
        environment.removeNode(node)
        environment.nodes.size shouldBeExactly 0
    }
})
