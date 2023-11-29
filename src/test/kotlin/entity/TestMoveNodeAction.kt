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

class TestMoveNodeAction : StringSpec({

    "move node action must move a node of one on both the x-axis and y-axis" {
        val radius = 5.0
        val linkingRule = PositionLinkingRule(radius)
        val environment = EnvironmentImpl(linkingRule = linkingRule)
        val node = NodeImpl(
            1,
        )
        environment.addNode(node, Position(0.0, 0.0))
        val action = MoveNodeAction(environment)
        action.execute()
        environment.getNodePosition(node) shouldBe Position(1.0, 1.0)
    }
})
