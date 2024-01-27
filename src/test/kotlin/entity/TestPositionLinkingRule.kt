/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestPositionLinkingRule : StringSpec({
    val radius = 5.0
    val environment = EnvironmentImpl()
    val linkingRule = PositionLinkingRule(radius, environment)
    environment.setLinkingRule(linkingRule)
    "linking rule must recognize that two nodes are close" {
        val node1 = NodeImpl(1)
        val node2 = NodeImpl(2)
        environment.addNode(node1, Position(0.0, 0.0))
        environment.addNode(node2, Position(0.0, 1.0))
        linkingRule.isNeighbor(node1, node2, environment) shouldBe true
    }

    "linking rule must recognize that two nodes aren't close" {
        val node1 = NodeImpl(3)
        val node2 = NodeImpl(4)
        environment.addNode(node1, Position(0.0, 0.0))
        environment.addNode(node2, Position(0.0, 7.0))
        linkingRule.isNeighbor(node1, node2, environment) shouldBe false
    }
})
