package org.firstinspires.ftc.teamcode.api

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import org.firstinspires.ftc.teamcode.RobotConfig.UniversalCoordinates
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


object UniversalCoordinates {

    fun inCloseTriangle(pose: DoubleArray): Boolean{
        var pos = doubleArrayOf(pose[0], pose[1])
        // If center is inside triangle
        if (pointInTriangle(pos, UniversalCoordinates.ctriangle_pos_1, UniversalCoordinates.ctriangle_pos_1, UniversalCoordinates.ctriangle_pos_1)) {
            return true
        }

        // 2. If circle intersects any edge
        if (distancePointToSegment(pos, UniversalCoordinates.ctriangle_pos_1, UniversalCoordinates.ctriangle_pos_2) <= UniversalCoordinates.RADIUS) return true
        if (distancePointToSegment(pos, UniversalCoordinates.ctriangle_pos_2, UniversalCoordinates.ctriangle_pos_3) <= UniversalCoordinates.RADIUS) return true
        if (distancePointToSegment(pos, UniversalCoordinates.ctriangle_pos_3, UniversalCoordinates.ctriangle_pos_1) <= UniversalCoordinates.RADIUS) return true

        // 3. Otherwise, completely outside
        return false
    }


    fun inFarTriangle(pose: DoubleArray): Boolean{
        var pos = doubleArrayOf(pose[0], pose[1])
        // If center is inside triangle
        if (pointInTriangle(pos, UniversalCoordinates.ftriangle_pos_1, UniversalCoordinates.ftriangle_pos_1, UniversalCoordinates.ftriangle_pos_1)) {
            return true
        }

        // If circle intersects any edge
        if (distancePointToSegment(pos, UniversalCoordinates.ftriangle_pos_1, UniversalCoordinates.ftriangle_pos_2) <= UniversalCoordinates.RADIUS) return true
        if (distancePointToSegment(pos, UniversalCoordinates.ftriangle_pos_2, UniversalCoordinates.ftriangle_pos_3) <= UniversalCoordinates.RADIUS) return true
        if (distancePointToSegment(pos, UniversalCoordinates.ftriangle_pos_3, UniversalCoordinates.ftriangle_pos_1) <= UniversalCoordinates.RADIUS) return true

        // outside
        return false
    }

    fun inParkingZone(pos: DoubleArray, side: String): Double{
        val minX: Double
        val maxX: Double
        val minY: Double
        val maxY: Double

        if (side == "Red") {
            minX = min(UniversalCoordinates.red_park[0], UniversalCoordinates.red_park[2])
            maxX = max(UniversalCoordinates.red_park[0], UniversalCoordinates.red_park[2])
            minY = min(UniversalCoordinates.red_park[1], UniversalCoordinates.red_park[3])
            maxY = max(UniversalCoordinates.red_park[1], UniversalCoordinates.red_park[3])
        } else {
            minX = min(UniversalCoordinates.blue_park[0], UniversalCoordinates.blue_park[2])
            maxX = max(UniversalCoordinates.blue_park[0], UniversalCoordinates.blue_park[2])
            minY = min(UniversalCoordinates.blue_park[1], UniversalCoordinates.blue_park[3])
            maxY = max(UniversalCoordinates.blue_park[1], UniversalCoordinates.blue_park[3])
        }

        // Fully inside
        if (
            pos[0] - UniversalCoordinates.RADIUS >= minX &&
            pos[0] + UniversalCoordinates.RADIUS <= maxX &&
            pos[1] - UniversalCoordinates.RADIUS >= minY &&
            pos[1] + UniversalCoordinates.RADIUS <= maxY
        ) {
            return 1.0
        }

        // Closest point on square to circle center
        val closestX = pos[0].coerceIn(minX, maxX)
        val closestY = pos[1].coerceIn(minY, maxY)

        val dx = pos[0] - closestX
        val dy = pos[1] - closestY
        val distance = distance(dx, dx, dy, dy)

        // Completely outside
        if (distance > UniversalCoordinates.RADIUS) {
            return 0.0
        }

        // partial overlap
        return 0.5
    }


    private fun pointInTriangle(p: DoubleArray, a: DoubleArray, b: DoubleArray, c: DoubleArray): Boolean {
        fun sign(p1: DoubleArray, p2: DoubleArray, p3: DoubleArray): Double {
            return (p1[0] - p3[0]) * (p2[1] - p3[1]) -
                    (p2[0] - p3[0]) * (p1[1] - p3[1])
        }

        val d1 = sign(p, a, b)
        val d2 = sign(p, b, c)
        val d3 = sign(p, c, a)

        val hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0)
        val hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0)

        return !(hasNeg && hasPos)
    }

    private fun distancePointToSegment(p: DoubleArray, v: DoubleArray, w: DoubleArray): Double {

        val l2 = (v[0] - w[0]).squared() + (v[1] - w[1]).squared()
        if (l2 == 0.0) {
            // v == w
            return distance(p[0], v[0], p[1], v[1])
        }

        val t = ((p[0] - v[0]).squared() + (p[1] - v[1]).squared()) / l2
        val tClamped = t.coerceIn(0.0, 1.0)

        return distance(p[0], v[0] + tClamped * (w[0] - v[0]), p[1],v[1] + tClamped * (w[1] - v[1]) )
    }

    /** Distance Formula */
    private fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double{
        return sqrt((x2-x1).squared()+(y2-y1).squared())
    }

    /** Midpoint Formula */
    private fun midpoint(x1: Double, y1: Double, x2: Double, y2: Double): DoubleArray{
        return doubleArrayOf((x1+x2)/2, (y1+y2)/2)
    }

}