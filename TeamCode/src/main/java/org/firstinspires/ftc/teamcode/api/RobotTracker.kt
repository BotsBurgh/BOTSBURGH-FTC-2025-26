package org.firstinspires.ftc.teamcode.api

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.core.API

/**
 * An API to track the current coordinates of the robot.
 */
object RobotTracker :API() {

    lateinit var tracker: SparkFunOTOS
        private set

    lateinit var pinpoint: GoBildaPinpointDriver
        private set

    lateinit var pose2D: Pose2D



    /**
     * Used for Initialization of RobotTracker during TeleOp.
     */
    fun teleInit(opMode: OpMode) {
        super.init(opMode)

        tracker = this.opMode.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")
        pinpoint = this.opMode.hardwareMap.get(GoBildaPinpointDriver::class.java,"deadwheel")

        configurePinpoint()

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(Pose2D(DistanceUnit.INCH, tracker.position.x, tracker.position.y, AngleUnit.DEGREES, tracker.position.h))
        pose2D = pinpoint.position
    }

    /**
     * Used for Initialization of RobotTracker during Autonomous. Does not initialize tracker.
     */
    fun autoInit(opMode: OpMode) {
        super.init(opMode)
    }


    fun configurePinpoint() {
        /*
        *  Set the odometry pod positions relative to the point that you want the position to be measured from.
        *
        *  The X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is.
        *  Left of the center is a positive number, right of center is a negative number.
        *
        *  The Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is.
        *  Forward of center is a positive number, backwards is a negative number.
        */
        pinpoint.setOffsets(
            -4.658,  // forward pod sideways offset
            7.866,
            DistanceUnit.INCH
        ) //these are tuned for 3110-0002-0001 Product Insight #1

        /*
         * Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
         * the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
         * If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
         * number of ticks per unit of your odometry pod.  For example:
         *     pinpoint.setEncoderResolution(13.26291192, DistanceUnit.MM);
         */
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)

        /*
         * Set the direction that each of the two odometry pods count. The X (forward) pod should
         * increase when you move the robot forward. And the Y (strafe) pod should increase when
         * you move the robot to the left.
         */


        //REVERSED REVERSED
        pinpoint.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.REVERSED,
            GoBildaPinpointDriver.EncoderDirection.FORWARD
        )

        /*
         * Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
         * The IMU will automatically calibrate when first powered on, but recalibrating before running
         * the robot is a good idea to ensure that the calibration is "good".
         * resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
         * This is recommended before you run your autonomous, as a bad initial calibration can cause
         * an incorrect starting value for x, y, and heading.
         */
        pinpoint.resetPosAndIMU()
    }


    /**
     * Updates the deadwheel position of the robot
     */
    fun updatePos(){
        pinpoint.update()
        pose2D = pinpoint.position
    }


    /**
     * Returns the position as an Array of Doubles, 0 = x, 1 = y, 2 = h
     *
     * @param isAuto specifies if the robot is in autonomous or teleOp
     */
    fun getPos(isAuto: Boolean):DoubleArray{
        if (isAuto) {
            return doubleArrayOf(SpecterDrive.otos.position.x, SpecterDrive.otos.position.y, SpecterDrive.otos.position.h)
        }
            //X and Y are flipped in the pinpoint becasuse of how its configured. To fit with our coordinate system, flip X and Y.
            return doubleArrayOf(pose2D.getY(DistanceUnit.INCH), pose2D.getX(DistanceUnit.INCH), pose2D.getHeading(AngleUnit.DEGREES))
    }

    /**
     * Gets the current velocity as an Array of Doubles, [0] = Horizontal, [1] = Vertical, [2] = Angular
     *
     * @param isAuto specifies if the robot is in autonomous or teleOp
     */
    fun getVelocity(isAuto: Boolean): DoubleArray{
        if (isAuto){
            return doubleArrayOf(SpecterDrive.otos.velocity.x, SpecterDrive.otos.velocity.y,
                SpecterDrive.otos.velocity.h)
        }
        //X and Y are flipped in the pinpoint becasuse of how its configured. To fit with our coordinate system, flip X and Y.
        return doubleArrayOf(pinpoint.getVelY(DistanceUnit.INCH), pinpoint.getVelX(DistanceUnit.INCH), pinpoint.getHeadingVelocity(
            UnnormalizedAngleUnit.DEGREES))
    }

    /**
     * ONLY WORKS IN AUTO
     * Gets the current acceleration as an Array of Doubles, [0] = Horizontal, [1] = Vertical, [2] = Radial
     **/
    fun getAcceleration(isAuto: Boolean): DoubleArray{
        return doubleArrayOf(SpecterDrive.otos.acceleration.x, SpecterDrive.otos.acceleration.y, SpecterDrive.otos.acceleration.h)

    }


    /**
     * Sets the current position.
     *
     * @param newX new X
     * @param newY new Y
     * @param newH new H
     * @param isAuto specifies if the robot is in autonomous or teleOp
     */
    fun setPos(newX : Double, newY: Double, newH : Double, isAuto: Boolean){
        if (isAuto) {
            SpecterDrive.otos.position = SparkFunOTOS.Pose2D(newX, newY, newH)
        }
        else {
            pinpoint.setPosX(newX, DistanceUnit.INCH)
            pinpoint.setPosY(newY, DistanceUnit.INCH)
            pinpoint.setHeading(newH, AngleUnit.DEGREES)
        }
    }

}