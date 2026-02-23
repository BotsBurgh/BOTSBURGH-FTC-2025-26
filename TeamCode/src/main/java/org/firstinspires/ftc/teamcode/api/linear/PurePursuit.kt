package org.firstinspires.ftc.teamcode.api.linear

import kotlin.math.*

object PurePursuit {
    /**
     * Data class for Pure Pursuit
     *
     * @param x field x coordinate for a point
     * @param y field y coordinate for a point
     * @param heading field heading coordinate that we want to end at (optional)
     */
    data class PathPoint(
        val x: Double,
        val y: Double,
        val heading: Double? = null
    )

    /**
     * Computes a path from a series of points and a radius
     *
     * @param path a list of PathPoints for the robot to drive to
     * @param lookahead a radius for the robot to spline around(6.0 - 8.0 is best)
     */
    class PurePursuit(
        private val path: List<PathPoint>,
        private val lookahead: Double = 6.0
    ) {

        private var currentSegment = 0

        fun getLookaheadPoint(robotX: Double, robotY: Double): PathPoint? {

            while (currentSegment < path.size - 1) {

                val p1 = path[currentSegment]
                val p2 = path[currentSegment + 1]

                val dx = p2.x - p1.x
                val dy = p2.y - p1.y

                val fx = p1.x - robotX
                val fy = p1.y - robotY

                val a = dx*dx + dy*dy
                val b = 2 * (fx*dx + fy*dy)
                val c = fx*fx + fy*fy - lookahead*lookahead

                val discriminant = b*b - 4*a*c

                if (discriminant >= 0) {
                    val sqrtDisc = sqrt(discriminant)

                    val t1 = (-b - sqrtDisc) / (2*a)
                    val t2 = (-b + sqrtDisc) / (2*a)

                    if (t2 in 0.0..1.0) {
                        return PathPoint(
                            p1.x + dx*t2,
                            p1.y + dy*t2,
                            p2.heading
                        )
                    }
                }

                currentSegment++
            }

            return path.last()
        }

        /**
         * Check to make sure if path is finished
         *
         * @param robotX the current X coordinate of the robot
         * @param robotY the current Y coordinate of the robot
         */

        fun isFinished(robotX: Double, robotY: Double, tolerance: Double = 1.0): Boolean {
            val end = path.last()
            return hypot(end.x - robotX, end.y - robotY) < tolerance
        }
    }
}