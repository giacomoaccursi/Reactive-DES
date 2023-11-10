/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Dummy implementation of a condition. It returns always true.
 */
class DummyCondition : Condition {
    override fun isValid() = true
}
