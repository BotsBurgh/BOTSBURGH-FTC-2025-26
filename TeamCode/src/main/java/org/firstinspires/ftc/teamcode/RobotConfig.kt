// We are treating config fields as constant even through they may be changed, which is why we use
// SCREAMING_SNAKE_CASE for them instead of `ktlint`'s desired lowerCamelCase.
@file:Suppress("ktlint:standard:property-naming")

package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.core.logging.Level
import java.io.File

/**
 * This is an immutable object representing robot configuration.
 *
 * It is meant to orchestrate FTC Dashboard and other configuration together. Certain sub-objects
 * are annotated with `@Config`. This designates them as FTC Dashboard configuration that can be
 * modified at runtime. **To permanently change these values, you must also modify the code!** The
 * configuration can also change during initialization depending on various build constants like
 * [DEBUG].
 */
object RobotConfig {
    /**
     * When true, enables debugging features like camera streaming and more logs.
     *
     * This should be disabled for competitions.
     */
    const val DEBUG: Boolean = true

    /**
     * Creates a string representing the current robot build constants.
     */
    override fun toString() = "RobotConfig(debug=$DEBUG)"

    /** Configuration related to the TeleOpMain opmode. */
    @Config
    object TeleOpMain {
        /** A multiplier that scales that robot's driving / strafing speed. */
        @JvmField
        var DRIVE_SPEED: Double = 0.85

        /** A multiplier that scales the robot's rotation speed. */
        @JvmField
        var ROTATE_SPEED: Double = 0.7

    }


    @Config
    object Logging {
        /**
         * The root folder for all Botsburgh-specific files, accessible at `/sdcard/BotsBurgh` on
         * the robot.
         */
        @JvmField
        var BOTSBURGH_FOLDER = File(AppUtil.ROOT_FOLDER, "/BotsBurgh")

        /** The folder where all logs are stored. */
        @JvmField
        var LOG_FOLDER = File(BOTSBURGH_FOLDER, "logs")

        @JvmField
        var TELEMETRY_CAPACITY = 5

        @JvmField
        var TELEMETRY_ORDER = Telemetry.Log.DisplayOrder.OLDEST_FIRST

        @JvmField
        var FILTER_LEVEL = Level.Info
    }


    @Config
    object OTOS {

        /**
         * If sensor is not mounted in the center and or facing the front of the robot,
         * adjust the offset. If possible mount in the center and avoid needing to change this.
         */
        @JvmField
        var OFFSET = SparkFunOTOS.Pose2D(-4.0, 0.0 ,90.0)

        /**
         * Should be set to one by default. If the sensor is reading distances incorrectly
         * the values can be changed to act as a correction (will only work if readings are
         * consistently off).
         */
        @JvmField
        var LINEAR_SCALAR: Double = 1.0

        /**
         * Should be set to one by default. If the sensor is reading heading incorrectly
         * the values can be changed to act as a correction (will only work if readings are
         * consistently off).
         */
        @JvmField
        var ANGULAR_SCALAR: Double = 1.0

        /**
         * Controls the acceleration of y-axis movement.
         */
        @JvmField
        var SPEED_GAIN: Double = 0.03


        /**
         * Controls the acceleration of x-axis movement.
         */
        @JvmField
        var STRAFE_GAIN: Double = 0.03


        /**
         * Controls the turning acceleration.
         */
        @JvmField
        var TURN_GAIN: Double = 0.04

        /**
         * Max speed that can be achieved by rotation.
         */
        @JvmField
        var MAX_AUTO_TURN: Double = 0.4


        /**
         * The accepted value for the x-axis error.
         */
        @JvmField
        var X_THRESHOLD: Double = 1.0

        /**
         * The accepted value for the y-axis error.
         */
        @JvmField
        var Y_THRESHOLD: Double = 1.0

        /**
         * The accepted value for the heading error.
         */
        @JvmField
        var H_THRESHOLD: Double = 4.0

        /**
         * Autonomous Speed
         */
        @JvmField
        var SPEED: Double = 0.5

        /**
         * The low power threshold
         */
        @JvmField
        var PWRTHRESHOLD: Double = 0.0
    }


    @Config
    object Turret {

        /**
         * Ticks in one degree of the aimer motor
         */
        @JvmField
        var TICKS_PER_DEGREE = 5.75

        /**
         * Gear ratio of the launcher motor
         */
        @JvmField
        var GEAR_RATIO_AIMER = 5.875

        /**
         * Ticks in one degree of the launcher motor
         */
        @JvmField
        var TICKS_PER_RADIANS = 23.55

        /**
         * Gear ratio of the launcher motor
         */
        @JvmField
        var GEAR_RATIO_LAUNCHER = 2.8

        /**
         * Servo Position
         */
        @JvmField
        var pos = 0.0

        @JvmField
        var pwr = 0.0

    }

    @Config
    object UniversalCoordinates{
        /**
         * Robot Constants
         */
        @JvmField
        var RADIUS = 8.25


        /**
         * Field-Centric positions of the robot on auto start
         */
        @JvmField
        var FAR_RED_POS = SparkFunOTOS.Pose2D(60.0, 12.0, 270.0)

        @JvmField
        var FAR_BLUE_POS = SparkFunOTOS.Pose2D(72.0, 12.0, 9.0)

        @JvmField
        var CLOSE_RED_POS = SparkFunOTOS.Pose2D(10.0, 134.0, 270.0)

        @JvmField
        var CLOSE_BLUE_POS = SparkFunOTOS.Pose2D(60.0, 12.0, 270.0)

        /**
         * Positions of the goals
         */
        @JvmField
        var BLUE_POS = doubleArrayOf(9.0, 144.0, 55.0)

        @JvmField
        var RED_POS = doubleArrayOf(135.0, 144.0, 55.0)

        /**
         * Triangle far positions
         */
        @JvmField
        var ftriangle_pos_1 = doubleArrayOf(48.0, 0.0)

        @JvmField
        var ftriangle_pos_2 = doubleArrayOf(60.0, 24.0)

        @JvmField
        var ftriangle_pos_3 = doubleArrayOf(72.0, 0.0)

        /**
         * Triangle close positions
         */
        @JvmField
        var ctriangle_pos_1 = doubleArrayOf(0.0, 144.0)

        @JvmField
        var ctriangle_pos_2 = doubleArrayOf(72.0, 144.0)

        @JvmField
        var ctriangle_pos_3 = doubleArrayOf(144.0, 144.0)

        /**
         * Parking Positions
         */
        @JvmField
        var red_park = doubleArrayOf(28.5, 24.0, 46.5, 42.0)

        @JvmField
        var blue_park = doubleArrayOf(115.5, 24.0, 97.5, 42.0)

        /**
         * Line positions for Blue
         */
        @JvmField
        var blue_line_far = doubleArrayOf(28.5, 35.5)

        @JvmField
        var blue_line_mid = doubleArrayOf(28.5, 59.25)

        @JvmField
        var blue_line_close = doubleArrayOf(28.5, 82.75)

        @JvmField
        var blue_gate = doubleArrayOf(10.0, 68.5)

        /**
         * Line positions for Red
         */
        @JvmField
        var red_line_far = doubleArrayOf(116.0, 35.5)

        @JvmField
        var red_line_mid = doubleArrayOf(116.0, 59.25)

        @JvmField
        var red_line_close = doubleArrayOf(116.0, 82.75)

        @JvmField
        var red_gate = doubleArrayOf(134.0, 68.5)

        /**
         * Human Player Zones
         */
        @JvmField
        var blue_zone = doubleArrayOf(0.0, 0.0, 24.0, 24.0)

        @JvmField
        var red_zone = doubleArrayOf(144.0, 0.0, 120.0, 24.0)



    }
}
