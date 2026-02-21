package org.firstinspires.ftc.teamcode.api.linear

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.MAX_AUTO_TURN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.SPEED
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.SPEED_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.STRAFE_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.OTOS.TURN_GAIN
import org.firstinspires.ftc.teamcode.RobotConfig.TeleOpMain.DRIVE_SPEED
import org.firstinspires.ftc.teamcode.RobotConfig.TeleOpMain.ROTATE_SPEED
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * An API for our Autonomous Pathing, initializes auto RobotTracking
 */

// Build Details: The OTOS sensor should be 8-10 mm from the ground. THIS IS A REQUIREMENT NOT A SUGGESTION.
// The sensor should be in the center of the robot, facing forward. You dont technically have to do this, but unless you want
// a fun time with offsets, its not a good idea to have an offset.
object SpecterDrive : API() {

    override val isLinear = true

    lateinit var otos: SparkFunOTOS
        private set

    private val runtime = ElapsedTime()

    private var xError: Double = 0.0
    private var yError: Double = 0.0
    private var hError: Double = 0.0
    private var turn: Double = 0.0

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
     * @param pose an array containing x and y coordinates
     */
    fun path(pose: DoubleArray, h: Double, t: Double = 999.99999, rC: Boolean = true) {
        path(pose[0], pose[1], h, t, rC)
    }



    /**
     * Computes a direct path towards a robot-centric position
     *
     * @param x The target global x coordinate on the field
     * @param y The target global y coordinate on the field
     * @param h The target global heading
     * @param t Max runtime in seconds before timing out
     * @param rC The centricity of the robot (assumes robot-centric unless specified)
     */
    fun path(x: Double, y: Double, h: Double, t: Double = 999.99999,  rC: Boolean = true) {

        var otosScreenshot = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)

        if(rC) {
            //takes a "screenshot" of the current position to add back
            otosScreenshot = otos.position
            //reset position to zero for robot-centric
            otos.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)
        }

        //set initial x, y, and h error
        xError = x - otos.position.x
        yError = y - otos.position.y
        hError = AngleUnit.normalizeDegrees(h - otos.position.h)

        //Reset time
        runtime.reset()

        //While time < timeout val and xError < x_threshold and yError < y_threshold and hError < h_threshold
        while (linearOpMode.opModeIsActive() && (runtime.milliseconds() < t * 1000) && ((abs(xError) > RobotConfig.OTOS.X_THRESHOLD) ||
                    (abs(yError) > RobotConfig.OTOS.Y_THRESHOLD) || (abs(hError) > RobotConfig.OTOS.H_THRESHOLD))
        ) {
            with(linearOpMode.telemetry) {
                addData("current X coordinate", otos.position.x)
                addData("current Y coordinate", otos.position.y)
                addData("current Heading angle", otos.position.h)
                update()
            }
            computePower(otos.position.h)

            // Update errors based on global position
            xError = x - otos.position.x
            yError = y - otos.position.y
            hError = AngleUnit.normalizeDegrees(h - otos.position.h)

        }
        //FullStop
        TriWheels.power(0.0, 0.0, 0.0)

        //readd position
        if(rC){
            otos.position.x += otosScreenshot.x
            otos.position.y += otosScreenshot.y
            otos.position.h += otosScreenshot.h
            AngleUnit.normalizeDegrees(otos.position.h)
        }
    }

    /**
     * Creates a vector for the robot to move to
     * @param currentHeading the current heading of the robot
     */
    private fun computePower(currentHeading: Double) {
        // Calculate the error vector in field coordinates
        val fieldX = -(xError * STRAFE_GAIN)
        val fieldY = -(yError * SPEED_GAIN)


        // Direction and magnitude
        val rad = atan2(fieldY, fieldX) - (PI / 3.0) - (2.0 * PI / 3.0)
        val magnitude = sqrt(fieldX * fieldX + fieldY * fieldY)

        var (r, g, b) = TriWheels.compute(rad, magnitude)

        turn = Range.clip(hError * TURN_GAIN * ROTATE_SPEED, -MAX_AUTO_TURN, MAX_AUTO_TURN)

        r += turn
        g += turn
        b += turn

        val max = maxOf(abs(r), abs(g), abs(b), 1.0)
        r /= max; g /= max; b /= max

        TriWheels.power(r, roundPower(g), roundPower(b))
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



    /**
     * Rotates to a specific global heading on the field.
     *
     * @param targetAngle The global degree you want the robot to face (e.g., 0 to 360 or -180 to 180)
     * @param power The maximum power to apply during rotation
     */
    fun rotateToHeading(targetAngle: Double, power: Double = 0.3) {
        val target = AngleUnit.normalizeDegrees(targetAngle)

        while (linearOpMode.opModeIsActive()) {
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

    fun log(msg: String){
        linearOpMode.telemetry.addLine(msg)
        linearOpMode.telemetry.update()
    }

}






