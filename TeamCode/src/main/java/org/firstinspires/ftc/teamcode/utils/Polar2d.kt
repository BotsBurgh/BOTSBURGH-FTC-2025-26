package org.firstinspires.ftc.teamcode.utils

import com.acmerobotics.roadrunner.DualNum
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.Vector2dDual
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Represents a coordinate in the
 * [polar coordinate system](https://en.wikipedia.org/wiki/Polar_coordinate_system).
 *
 * [theta] is the relative angle from 0 in radians on the unit circle. (A theta of 0 points to the
 * right, not up.) [radius] is the distance from the center, used to represent strength or
 * magnitude.
 *
 * For a alternative to this that uses the cartesian coordinate system (x, y), see [Vector2d].
 */
data class Polar2d(val theta: Double, val radius: Double) {
    companion object {
        /** Converts cartesian coordinates into polar coordinates. */
        fun fromCartesian(
            x: Double,
            y: Double,
        ): Polar2d {
            // Inverse tangent to find the angle, except it special-cases specific circumstances.
            val theta = atan2(y, x)

            // Calculate the hypotenuse (distance) using sqrt(x^2 + y^2).
            val radius = hypot(x, y)

            return Polar2d(theta, radius)
        }

        /** Converts cartesian coordinates into polar coordinates. */
        fun fromCartesian(cartesian: Vector2d): Polar2d = fromCartesian(cartesian.x, cartesian.y)
    }

    /** Returns the cartesian form of these polar coordinates. */
    fun toCartesian() =
        Vector2d(
            this.radius * cos(this.theta),
            this.radius * sin(this.theta),
        )
}

/** The dual number variant of [Polar2d]. */
data class Polar2dDual<Param>(val theta: DualNum<Param>, val radius: DualNum<Param>) {
    companion object {
        /** Creates a new [Polar2dDual] from a constant [Polar2d] and a size. */
        fun <Param> constant(
            p: Polar2d,
            n: Int,
        ) = Polar2dDual<Param>(DualNum.constant(p.theta, n), DualNum.constant(p.radius, n))

        fun <Param> fromCartesian(v: Vector2dDual<Param>): Polar2dDual<Param> {
            // Calculate the slope.
            val theta = v.atan2()

            // Calculate the hypotenuse.
            val radius = (v.x * v.x + v.y * v.y).sqrt()

            return Polar2dDual(theta, radius)
        }
    }

    /** Returns the cartesian form of these polar coordinates. */
    fun toCartesian() =
        Vector2dDual(
            this.radius * this.theta.cos(),
            this.radius * this.theta.sin(),
        )

    /** Returns the value of this polar coordinate. */
    fun value() = Polar2d(this.theta.value(), this.radius.value())
}
