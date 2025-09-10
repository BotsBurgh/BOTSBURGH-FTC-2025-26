package org.firstinspires.ftc.teamcode.api

import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.utils.PI_2
import org.firstinspires.ftc.teamcode.utils.Polar2d
import kotlin.math.PI
import kotlin.math.sin

/**
 * An API for controlling the wheels of the triangle robot.
 */
object TriWheels : API() {
    lateinit var red: DcMotorEx
        private set
    lateinit var green: DcMotorEx
        private set
    lateinit var blue: DcMotorEx
        private set

    // The angles of each wheel in radians, where red is forward.
    const val RED_ANGLE: Double = PI / 2.0
    const val GREEN_ANGLE: Double = PI * (11.0 / 6.0)
    const val BLUE_ANGLE: Double = PI * (7.0 / 6.0)

    override fun init(opMode: OpMode) {
        super.init(opMode)

        red = this.opMode.hardwareMap.get(DcMotorEx::class.java, "redWheel")
        green = this.opMode.hardwareMap.get(DcMotorEx::class.java, "greenWheel")
        blue = this.opMode.hardwareMap.get(DcMotorEx::class.java, "blueWheel")

        stopAndResetMotors()
    }

    /**
     * Sets the power of each wheel respectively.
     */
    fun power(
        redPower: Double,
        greenPower: Double,
        bluePower: Double,
    ) {
        red.power = redPower
        green.power = greenPower
        blue.power = bluePower
    }

    /**
     * Rotates all of the wheels with a given [power].
     */
    fun rotate(power: Double) {
        power(power, power, power)
    }

    /**
     * Makes the robot drive in a certain direction [radians] with a given strength [magnitude].
     *
     * If [rotation] is specified, it acts as a offset applied to each wheel to make the entire
     * robot spin.
     */
    fun drive(
        radians: Double,
        magnitude: Double,
        rotation: Double = 0.0,
    ) {
        val (r, g, b) = compute(radians, magnitude)
        power(r + rotation, g + rotation, b + rotation)
    }

    fun drive(
        polar: Polar2d,
        rotation: Double = 0.0,
    ) {
        val (r, g, b) = compute(polar)
        power(r + rotation, g + rotation, b + rotation)
    }

    /**
     * Makes all 3 wheels stop.
     *
     * This is shorthand for setting the power to 0.
     */
    fun stop() {
        power(0.0, 0.0, 0.0)
    }

    /**
     * This function resets all 3 motors.
     *
     * That includes:
     *
     * - Their speed.
     * - Their encoder position.
     * - Their run mode.
     * - Their braking behavior.
     * - Their direction.
     *
     * If you manually changed something like a motor's direction, you'll have to do it again after
     * calling this function.
     */
    fun stopAndResetMotors() {
        stop()

        for (motor in this.wheels()) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            motor.direction = DcMotorSimple.Direction.FORWARD
        }
    }

    /**
     * Returns the ratio of how much each wheel should spin for given angle in [radians] and
     * strength in [magnitude].
     *
     * For a visual demonstration of what this is doing, see
     * [this Desmos graph](https://www.desmos.com/geometry/b6h0f7fjls).
     */
    fun compute(
        radians: Double,
        magnitude: Double,
    ): Triple<Double, Double, Double> =
        Triple(
            magnitude * sin(RED_ANGLE - radians),
            magnitude * sin(GREEN_ANGLE - radians),
            magnitude * sin(BLUE_ANGLE - radians),
        )

    fun compute(polar: Polar2d) = compute(polar.theta, polar.radius)

    fun inverse(ratio: Triple<Double, Double, Double>): Vector2d {
        val r = Polar2d(RED_ANGLE - PI_2, ratio.first).toCartesian()
        val g = Polar2d(GREEN_ANGLE - PI_2, ratio.second).toCartesian()
        val b = Polar2d(BLUE_ANGLE - PI_2, ratio.third).toCartesian()

        // For some reason there's a coefficient of 1.5 that needs to be divided out.
        return (r + g + b) / 1.5
    }

    /** Returns an array containing [red], [green], and [blue]. */
    fun wheels() = arrayOf(this.red, this.green, this.blue)

    fun halfSpeed(possible: Boolean){
            if(possible) {
                RobotConfig.TeleOpMain.ROTATE_SPEED /= RobotConfig.TeleOpMain.SPEED_MODIFIER
                RobotConfig.TeleOpMain.DRIVE_SPEED /= RobotConfig.TeleOpMain.SPEED_MODIFIER
            }
    }

    fun fullSpeed(possible: Boolean){
        if(!possible) {
            RobotConfig.TeleOpMain.ROTATE_SPEED *= RobotConfig.TeleOpMain.SPEED_MODIFIER
            RobotConfig.TeleOpMain.DRIVE_SPEED *= RobotConfig.TeleOpMain.SPEED_MODIFIER
        }
    }
}
