/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

/**
 * Describes the possible context for a given action or condition.
 * A context represent the elements wichh are read for a condition and the elements
 * that may be subject of modification for an action.
 */
enum class Context {
    /**
     * The event can influence every other event.
     */
    GLOBAL,

    /**
     * The event can influence its node and the neighboring ones.
     */
    NEIGHBORHOOD,

    /**
     * The event can influence only the node in which it's placed.
     */
    LOCAL,
}
