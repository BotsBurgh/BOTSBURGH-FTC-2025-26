package org.firstinspires.ftc.teamcode.api

import java.io.File
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API

/*
An API to track the current coordinates of the robot.
 */
object RobotTracker :API() {

    lateinit var tracker: SparkFunOTOS
        private set

    //The autonomous position of the robot. We can't use tracker directly because it is being used by SpecterDrive.
    var autoX : Double = 0.0
    var autoY : Double = 0.0
    var autoH : Double = 0.0


    /**
     * Used for Initialization of RobotTracker during TeleOp.
     */
    fun teleInit(opMode: OpMode) {
        super.init(opMode)

        tracker = this.opMode.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")

        readPositionFile()
        configureOtos()
    }

    /**
     * Used for Initialization of RobotTracker during Autonomous. Does not initialize tracker
     */
    fun autoInit(opMode: OpMode) {
        super.init(opMode)

        CsvLogging.createFile("Position")
    }

    /**
     * Configures the OTOS sensor, identical to configureOTOS in Specterdrive
     */
    private fun configureOtos() {
        //Sets the desired units for linear and angular movement. Currently
        // set to INCH and DEGREES but can be set to CM or RADIANS as well.
        tracker.setLinearUnit(DistanceUnit.INCH)
        tracker.setAngularUnit(AngleUnit.DEGREES)

        tracker.offset = RobotConfig.OTOS.OFFSET

        tracker.linearScalar = RobotConfig.OTOS.LINEAR_SCALAR
        tracker.angularScalar = RobotConfig.OTOS.ANGULAR_SCALAR

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
            return doubleArrayOf(autoX, autoY, autoH)
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
            autoX = newX
            autoY = newY
            autoH = newH
        }
        else {
            tracker.position.x = newX
            tracker.position.y = newY
            tracker.position.h = newH
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
            autoX += deltaX
            autoY += deltaY
            autoH += deltaH
        }
        else {
            tracker.position.x += deltaX
            tracker.position.y += deltaY
            tracker.position.h += deltaH
        }
    }


    /**
     * Logs the current X, Y, and H to the Position CSV file. Call at the very end of Auto.
     * We need this because at the end of autonomous, the program has to terminate and in teleOP, starts again.
     * This resets all variables in the program, meaning that we have to log the variables at the end of Autonomous
     * so we can read them in the beginning of teleOp.
     *
     * @param startSide the side in which the robot starts. 0.0 = red, 1.0 = blue
     * @param startPos the further away the robot is at start, the more the value is. 0.0 = close to obelisk, 1.0 far from obelisk
     */
    fun logPos(startSide: Double, startPos: Double){
        //Write the file
        CsvLogging.writeFile("Position", arrayOf(getPos(true)[0], getPos(true)[1], getPos(true)[2], startSide, startPos))
        //Flush
        CsvLogging.flush("Position")
        //Close writer
        CsvLogging.close()
    }


    /**
     * Reads the Position file, and sets the current position accordingly.
     * Use at the beginning of teleOP
     */
    fun readPositionFile(){
        //Turn the first line of the CSV into a string
        val line = File("Control Hub v1.0\\Internal shared storage\\BotsBurgh\\Position.csv").bufferedReader().use { it.readLine() }
        //Make a regex(regular expression), this will split the line into useable double values
        val regex = "[,]"

        //Split the line into a String List, x located at [0], y at [1], h at [2]
        val arrPos = line.split(regex)

        //Set the position
        setPos(arrPos[0].toDouble(), arrPos[1].toDouble(), arrPos[2].toDouble(), false)
    }

}