package org.firstinspires.ftc.teamcode.api.linear

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.MAX_AUTO_TURN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.SPEED_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.STRAFE_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.TURN_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.TeleOpMain.ROTATE_SPEED
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

import kotlin.math.sqrt

/**
 * An API for our Autonomous Pathing, initializes auto RobotTracking
 */

// Build Details: The OTOS sensor should be 8-10 mm from the ground. THIS IS A REQUIREMENT NOT A SUGGESTION.
// The sensor should be in the center of the robot, facing forward. There is an X and Y arrow on the PCB to help with alignment
// You dont technically have to do this, but unless you want a hard time with offsets, its not a good idea to have an offset.
object SpecterDrive : API() {

    override val isLinear = true

    lateinit var otos: SparkFunOTOS
        private set

    private val runtime = ElapsedTime()

    private var xError: Double = 0.0
    private var yError: Double = 0.0
    private var hError: Double = 0.0
    private var turn: Double = 0.0
    private var previousHError: Double = 0.0
    private var previousTime = 0.0
    var isLog: Boolean = false

    override fun init(opMode: OpMode) {
        super.init(opMode)
        RobotTracker.autoInit(opMode)

        otos = this.opMode.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")

        configureOtos()
    }


    /**
     * Run during init for proper initialization of the otos sensor
     */
    private fun configureOtos() {
        linearOpMode.telemetry.addLine("OTOS Config")
        linearOpMode.telemetry.update()

        //Sets the desired units for linear and angular movement. Currently
        // set to INCH and DEGREES but can be set to CM or RADIANS as well.
        otos.setLinearUnit(DistanceUnit.INCH)
        otos.setAngularUnit(AngleUnit.DEGREES)

        otos.offset = RobotConfig.OTOS.OFFSET

        otos.linearScalar = RobotConfig.OTOS.LINEAR_SCALAR
        otos.angularScalar = RobotConfig.OTOS.ANGULAR_SCALAR

        otos.calibrateImu()

        otos.resetTracking()

        otos.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)

        if (isLog) {
            CsvLogging.init(opMode)
            CsvLogging.createFile("OTOS")
        }
    }

    /**
     * Computes a direct path towards a robot-centric position
     *
     * @param x The target global x coordinate on the field
     * @param y The target global y coordinate on the field
     * @param h The target global heading
     * @param t Max runtime in seconds before timing out
     * @param rC The centricity of the robot (assumes robot-centric unless specified)
     * @param spMag A special magnetude to travel with
     */
    fun path(x: Double, y: Double, h: Double, t: Double = 999.99999, rC: Boolean = true, spMag: Double = 0.0): DoubleArray {

        val targetX: Double
        val targetY: Double
        val targetH: Double = h

        if (rC) {
            // If robot-centric, the x/y passed are offsets.
            // We add them to the current global position to find the field target.
            targetX = otos.position.x + x
            targetY = otos.position.y + y
        } else {
            // If field-centric, the x/y passed are the actual field coordinates.
            targetX = x
            targetY = y
        }

        // Set initial errors based on the calculated target
        xError = targetX - otos.position.x
        yError = targetY - otos.position.y
        hError = AngleUnit.normalizeDegrees(targetH - otos.position.h)
        previousHError = 0.0

        runtime.reset()

        while (linearOpMode.opModeIsActive() && (runtime.milliseconds() < t * 1000) &&
            ((abs(xError) > RobotConfig.OTOS.X_THRESHOLD) ||
                    (abs(yError) > RobotConfig.OTOS.Y_THRESHOLD) ||
                    (abs(hError) > RobotConfig.OTOS.H_THRESHOLD))) {

            if (spMag == 0.0) computePower() else computePower(spMag)

            // Update errors using the consistent global targets
            xError = targetX - otos.position.x
            yError = targetY - otos.position.y
            hError = AngleUnit.normalizeDegrees(targetH - otos.position.h)

            linearOpMode.idle()
        }

        TriWheels.power(0.0, 0.0, 0.0)
        return doubleArrayOf(otos.position.x, otos.position.y, otos.position.h)
    }

    /**
     * Computes a direct path towards a robot-centric position
     *
     * @param pose an array containing x and y coordinates
     */
    fun path(pose: DoubleArray, h: Double, t: Double = 999.99999, rC: Boolean = true) {
        path(pose[0], pose[1], h, t, rC)
    }

    /**
     * Rotates to a specific global heading on the field.
     *
     * @param targetAngle The global degree you want the robot to face (e.g., 0 to 360 or -180 to 180)
     * @param power The maximum power to apply during rotation
     * @param timeout timeout
     */
    fun rotateToHeading(targetAngle: Double, power: Double = 0.3, timeout: Double = 3.0) {
        runtime.reset()
        val target = AngleUnit.normalizeDegrees(targetAngle)

        while (linearOpMode.opModeIsActive() && runtime.seconds() < timeout) {
            // Calculate the shortest distance to the target angle
            val error = AngleUnit.normalizeDegrees(target - otos.position.h)

            // Exit loop if we are within 1 degree of the target
            if (abs(error) < RobotConfig.OTOS.H_THRESHOLD) break

            val turnPower = power * kotlin.math.sign(error)

            TriWheels.rotate(turnPower)

            linearOpMode.idle()
        }

        // Stop the motors
        TriWheels.power(0.0, 0.0, 0.0)
    }

    /**
     * Updates the OTOS internal position to match field coordinates.
     */
    fun setPose(x: Double, y: Double, h: Double) {
        otos.position = SparkFunOTOS.Pose2D(x, y, h)
    }

    /**
     * Follows a PurePursuit path
     *
     * @param pursuit the path to follow
     * @param timeout the timeout
     */
    fun followPurePursuit(pursuit: PurePursuit, timeout: Double = 10.0) {
        runtime.reset()

        while (linearOpMode.opModeIsActive() && runtime.seconds() < timeout) {

            val robotX = otos.position.x
            val robotY = otos.position.y

            val lookahead = pursuit.getLookaheadPoint(robotX, robotY) ?: break
            val targetH = lookahead.heading ?: otos.position.h

            xError = lookahead.x - robotX
            yError = lookahead.y - robotY
            hError = AngleUnit.normalizeDegrees(targetH - otos.position.h)

            computePower()
            linearOpMode.idle()
        }

        TriWheels.power(0.0, 0.0, 0.0)
    }


    /**
     * Creates a vector for the robot to move to
     * @param mag the optional magnetude of the robot
     */
    private fun computePower(mag: Double = 0.0) {
        // 1. Calculate the error in Field Coordinates
        val fx = xError * STRAFE_GAIN
        val fy = yError * SPEED_GAIN

        // 2. Rotate the vector into Robot Coordinates
        val robotHeadingRad = Math.toRadians(otos.position.h)
        val rotX = fx * Math.cos(-robotHeadingRad) - fy * Math.sin(-robotHeadingRad)
        val rotY = fx * Math.sin(-robotHeadingRad) + fy * Math.cos(-robotHeadingRad)

        // 3. Calculate direction based on Robot-Relative coordinates
        val rad = atan2(rotY, rotX) - PI
        val magnitude = if (mag == 0.0) sqrt(rotX * rotX + rotY * rotY) else mag

        var (r, g, b) = TriWheels.compute(rad, magnitude)

        // Apply turn logic
        val currentTime = runtime.seconds()
        val dt = currentTime - previousTime

        val derivative = if (dt > 0) (hError - previousHError) / dt else 0.0

        val turn = Range.clip(
            (hError * TURN_GAIN) + (derivative * 0.01/*Derivitave gain*/),
            -MAX_AUTO_TURN,
            MAX_AUTO_TURN
        )

        previousHError = hError
        previousTime = currentTime
        r += turn
        g += turn
        b += turn

        // Normalize and Power
        val max = maxOf(abs(r), abs(g), abs(b), 1.0)
        TriWheels.power(roundPower(r/max), roundPower(g/max), roundPower(b/max))
    }


    /**
     * Rounds power to 0 in case of math returning miniscule powers
     *
     * @param pwr the raw power given to the wheel
     */
    private fun roundPower(pwr: Double): Double {
        return if (abs(pwr) < RobotConfig.OTOS.PWRTHRESHOLD) {
            0.0
        } else {
            pwr
        }
    }

}