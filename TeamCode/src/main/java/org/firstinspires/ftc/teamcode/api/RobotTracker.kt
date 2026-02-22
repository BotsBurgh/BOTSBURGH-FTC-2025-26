package org.firstinspires.ftc.teamcode.api

import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

/**
 * An API to track the current coordinates of the robot.
 */
object RobotTracker :API() {

    lateinit var tracker: SparkFunOTOS
        private set


    /**
     * Used for Initialization of RobotTracker during TeleOp.
     */
    fun teleInit(opMode: OpMode) {
        super.init(opMode)

        tracker = this.opMode.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")

        configureOtos()
    }

    /**
     * Used for Initialization of RobotTracker during Autonomous. Does not initialize tracker.
     */
    fun autoInit(opMode: OpMode) {
        super.init(opMode)
    }

    /**
     * Configures the OTOS sensor, identical to configureOTOS in Specterdrive
     */
    private fun configureOtos() {
        //Sets the desired units for linear and angular movement. Currently
        // set to INCH and DEGREES but can be set to CM or RADIANS as well.
        tracker.setLinearUnit(DistanceUnit.INCH)
        tracker.setAngularUnit(AngleUnit.DEGREES)

        tracker.offset = SparkFunOTOS.Pose2D(-4.0, 0.0 ,90.0)

        tracker.linearScalar = 1.0
        tracker.angularScalar = 1.0

        tracker.calibrateImu()

        tracker.resetTracking()

        tracker.position = SparkFunOTOS.Pose2D(0.0, 0.0, 0.0)
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
            return doubleArrayOf(tracker.position.x, tracker.position.y, tracker.position.h)
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
            tracker.position = SparkFunOTOS.Pose2D(newX, newY, newH)
        }
    }

    /**
     * Adds a difference in X, Y, and H to the current position.
     *
     * @param deltaX difference in x
     * @param deltaY difference in y
     * @param deltaH difference in h
     * @param isAuto specifies if the robot is in autonomous or teleOp
     */
    fun addPos(deltaX: Double, deltaY: Double, deltaH: Double, isAuto: Boolean){
        if (isAuto) {
            SpecterDrive.otos.position.x += deltaX
            SpecterDrive.otos.position.y += deltaY
            SpecterDrive.otos.position.h += deltaH
        }
        else {
            tracker.position.x += deltaX
            tracker.position.y += deltaY
            tracker.position.h += deltaH
        }
    }



    /**
     * Reads the Position file, and sets the current position accordingly.
     * Use at the beginning of teleOP
     */
    fun readPositionFile() : DoubleArray{
        val file = AppUtil.getInstance().getSettingsFile("Position.csv")

        if (!file.exists()){
            setPos(0.0, 0.0, 0.0, false)
            return doubleArrayOf(0.0, 0.0, 0.0, 0.0, 1.0)
        }

        val line = file.bufferedReader().use { it.readLine() }

        val arr = line.split(",")

        setPos(arr[0].toDouble(), arr[1].toDouble(), arr[2].toDouble(), false)

        return doubleArrayOf(arr[0].toDouble(), arr[1].toDouble(), arr[2].toDouble(), arr[3].toDouble(), arr[4].toDouble())
    }

}