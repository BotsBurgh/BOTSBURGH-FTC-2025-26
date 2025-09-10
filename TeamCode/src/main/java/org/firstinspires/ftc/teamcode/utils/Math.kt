package org.firstinspires.ftc.teamcode.utils

import com.acmerobotics.roadrunner.DualNum
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.Vector2dDual
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sign
import kotlin.math.withSign

/** PI / 2 */
const val PI_2: Double = PI / 2.0

/**
 * Rounds a number to a certain number of decimal places.
 *
 * This can be useful when comparing two doubles, since floating point operations tend to cause
 * small amounts of drift.
 *
 * For example, `(3.222).roundDecimal(1) == 3.2`. We're truncating to the 1st decimal place, rounding
 * the rest down. On the other hand, `(0.9999).roundDecimal(3) == 1.0` because the 4th 9 rounds up.
 */
fun Double.roundDecimal(places: Int): Double {
    val k = 10.0.pow(places)
    return round(this * k) / k
}

/** Applies function [f] to both [Vector2d.x] and [Vector2d.y], returning the result. */
inline fun Vector2d.map(f: (Double) -> Double) = Vector2d(f(this.x), f(this.y))

/** Returns the inverse tangent of a dual number. */
fun <Param> DualNum<Param>.atan(): DualNum<Param> {
    // Please see https://github.com/BotsBurgh/BOTSBURGH-FTC-2024-25/wiki/On-dual-numbers for an
    // explanation of the code, and a guide on creating your own.

    val x = this.values().toDoubleArray()
    val y = DoubleArray(x.size)

    if (y.isEmpty()) return DualNum(y)

    // Constant value
    y[0] = kotlin.math.atan(x[0])
    if (y.size == 1) return DualNum(y)

    // First derivative
    val firstDeriv = 1 / (x[0].squared() + 1)
    y[1] = firstDeriv * x[1]
    if (y.size == 2) return DualNum(y)

    // Second derivative
    val secondDeriv = (-2 * x[0]) / (x[0].squared() + 1).squared()
    y[2] = secondDeriv * x[1] + firstDeriv * x[2]
    if (y.size == 3) return DualNum(y)

    // Third derivative
    val thirdDeriv = (6 * x[0].squared() - 2) / (x[0].squared() + 1).cubed()
    y[3] = thirdDeriv * x[1] + secondDeriv * x[2] + secondDeriv * x[2] + firstDeriv * x[3]

    return DualNum(y)
}

/**
 * Returns the inverse tangent of a [Vector2dDual].
 *
 * This corresponds to [kotlin.math.atan2], but does not implement special casing for infinity.
 */
fun <Param> Vector2dDual<Param>.atan2(): DualNum<Param> {
    fun dmc(c: Double) = DualNum.constant<Param>(c, this.size)

    val (x, y) = this.value()

    // Special case for when the slope is 0.
    if (y == 0.0) {
        if (x == 0.0) return dmc(0.0)

        return if (x > 0.0) {
            dmc(0.0.withSign(y.sign))
        } else {
            dmc(PI.withSign(y.sign))
        }
    }

    return (this.y / this.x).atan()
}

/** Returns the minimum size of a [Vector2dDual]'s components. */
private val <Param> Vector2dDual<Param>.size
    get() = min(this.x.size(), this.y.size())

/** Squares a number `x^2`. */
private fun Double.squared() = this * this

/** Cubes a number `x^3`. */
private fun Double.cubed() = this * this * this
