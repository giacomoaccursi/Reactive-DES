/*
 * Copyright (c) 2023. Accursi Giacomo
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package entity

import java.awt.geom.Point2D

/**
 * Represent a position in a cartesian plane.
 * @param x the x coordinate.
 * @param y the y coordinate.
 */
data class Position(private val x: Double, private val y: Double) {
    /**
     * Gets the distance from another position.
     * @param other the other position.
     * @return the distance.
     */
    fun distanceTo(other: Position) = Point2D.distance(x, y, other.x, other.y)

    /**
     * Sums from a position another position.
     * @param positionToSum the position to sum.
     * @return the sum of the positions.
     */
    fun sum(positionToSum: Position) = Position(x + positionToSum.x, y + positionToSum.y)
}
