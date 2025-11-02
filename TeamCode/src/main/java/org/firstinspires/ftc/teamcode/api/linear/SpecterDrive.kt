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
import org.firstinspires.ftc.teamcode.RobotConfig.TeleOpMain.DRIVE_SPEED
import org.firstinspires.ftc.teamcode.RobotConfig.TeleOpMain.ROTATE_SPEED
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

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
     * Computes a direct path towards the targeted position
     *
     * @param x The target global x coordinate on the field
     * @param y The target global y coordinate on the field
     * @param h The target global heading
     * @param t Max runtime in seconds before timing out
     */
    fun path(x: Double, y: Double, h: Double, t: Double) {
        //Reset tracking for otos
        otos.resetTracking()

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
            computePower()

            //update X, Y, and H-error
            xError = x - otos.position.x
            yError = y - otos.position.y
            hError = AngleUnit.normalizeDegrees(h - otos.position.h)

            //Telemetry logging
            with(linearOpMode.telemetry) {
                addData("current X coordinate", otos.position.x)
                addData("current Y coordinate", otos.position.y)
                addData("current Heading angle", otos.position.h)
                addData("target X coordinate", x)
                addData("target Y coordinate", y)
                addData("target Heading angle", h)
                addData("xError", xError)
                addData("yError", yError)
                addData("yawError", hError)
                addData("Turn", turn)
                update()
            }

            //Csv logging
            if (isLog) {
                CsvLogging.writeFile(
                    "OTOS",
                    arrayOf(
                        otos.position.x,
                        xError,
                        otos.position.y,
                        yError,
                        otos.position.h,
                        hError,
                        turn
                    )
                )
                CsvLogging.flush("OTOS")
            }

        }
        //FullStop
        TriWheels.power(0.0, 0.0, 0.0)

        //Reset Position for new movement
        otos.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)

        //Close CSV
        CsvLogging.close()
    }

    /**
     * Computes a linear path towards the targeted position, with no timeout or logging
     *
     * @param x The target global x coordinate on the field
     * @param y The target global y coordinate on the field
     * @param h The target global heading
     */
    fun linearPath(x: Double, y: Double, h: Double = 0.0) {
        //Reset tracking for otos
        otos.resetTracking()

        //set initial x, y, and h error
        xError = x - otos.position.x
        yError = y - otos.position.y
        hError = AngleUnit.normalizeDegrees(h - otos.position.h)

        //Reset time
        runtime.reset()

        //While  xError < x_threshold and yError < y_threshold and hError < h_threshold
        while (linearOpMode.opModeIsActive()  && ((abs(xError) > RobotConfig.OTOS.X_THRESHOLD) ||
                    (abs(yError) > RobotConfig.OTOS.Y_THRESHOLD) || (abs(hError) > RobotConfig.OTOS.H_THRESHOLD))
        ) {
            computePower()

            //update X, Y, and H-error
            xError = x - otos.position.x
            yError = y - otos.position.y
            hError = AngleUnit.normalizeDegrees(h - otos.position.h)

        }
        //FullStop
        TriWheels.power(0.0, 0.0, 0.0)

        //Reset Position for new movement
        otos.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)

        //Close CSV
        CsvLogging.close()
    }

    /**
     * Calculates the powers to follow any given path. All powers are normalized and adjusted with Drive and Rotate Speed
     */
    private fun computePower() {
        // Adjusted Vector Components (apply gains)
        val adjX = -(xError * STRAFE_GAIN)
        val adjY = -(yError * SPEED_GAIN)

        // Direction and magnitude of vector
        val rad = atan2(adjY, adjX) - (PI / 3.0) - (2.0 * PI / 3.0)
        val magnitude = sqrt(adjX * adjX + adjY * adjY)

        // Compute translation powers
        var (r, g, b) = TriWheels.compute(rad, magnitude * DRIVE_SPEED)

        // Compute rotation
        turn = Range.clip(hError * TURN_GAIN * ROTATE_SPEED, -MAX_AUTO_TURN, MAX_AUTO_TURN)

        // Add turn value
        r += turn
        g += turn
        b += turn

        // Normalize so no wheel exceeds ±1
        val max = maxOf(abs(r), abs(g), abs(b), 1.0)
        r /= max; g /= max; b /= max

        //Power
        TriWheels.power(r, g, b)
    }

    /**
     * Computes a quadratic path towards the targeted position, with the origin being the current position
     */

    /**
     * Rotates until reaches a certain position
     *
     * @param d: the degree the robot rotates to, assuming that its current degree is 0
     * @param p: the power which the robot rotates with towards said degree, cannot be 0.0, recommended low value
     */

    fun rotate(d :Double, p :Double = 0.1) {
        //reset tracking
        otos.resetTracking()

        while (linearOpMode.opModeIsActive() && otos.position.h <= AngleUnit.normalizeDegrees(d)) {
            TriWheels.rotate(p)
        }

        //Stop rotation
        TriWheels.power(0.0, 0.0, 0.0)

        //Reset Position
        otos.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)
    }





}
