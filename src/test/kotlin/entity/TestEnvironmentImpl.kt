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
        val radius = 5.0
        val linkingRule = PositionLinkingRule(radius)
        val environment = EnvironmentImpl(linkingRule = linkingRule)
        println("dopo")
        environment.addNode(NodeImpl(1), Position(0.0, 0.0))
        environment.addNode(NodeImpl(2), Position(0.0, 0.0))
        environment.nodes.size shouldBeExactly 2
    }

    "nodes are removed correctly from the environment" {
        val radius = 5.0
        val linkingRule = PositionLinkingRule(radius)
        val environment = EnvironmentImpl(linkingRule = linkingRule)
        val node = NodeImpl(1)
        environment.addNode(node, Position(0.0, 0.0))
        environment.removeNode(node)
        environment.nodes.size shouldBeExactly 0
    }
})
