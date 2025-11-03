package org.firstinspires.ftc.teamcode.api

import java.io.File
import com.qualcomm.hardware.sparkfun.SparkFunOTOS
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API

/*
An API to track the current coordinates of the robot, tracks both robot and field-centric
 */
object RobotTracker :API() {

    var x = 0.0
    var y = 0.0
    var h = 0.0

    lateinit var tracker: SparkFunOTOS
        private set

    override fun init(opMode: OpMode) {
        super.init(opMode)
        CsvLogging.init(opMode)

        tracker = this.opMode.hardwareMap.get(SparkFunOTOS::class.java, "OTOS")

        configureOtos()
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

        CsvLogging.createFile("Position")
    }


    /**
     * Returns the current position as an Array of Doubles, 0 = x, 1 = y, 2 = h
     */
    fun getPos():DoubleArray{
        return doubleArrayOf(x, y, h)
    }

    /**
     * Sets the current position.
     *
     * @param deltaX: difference in x
     * @param deltaY: difference in y
     * @param deltaH: difference in h
     */
    fun setPos(deltaX : Double, deltaY: Double, deltaH : Double){
        x = deltaX
        y = deltaY
        h = deltaH
    }

    /**
     * Adds a difference in X, Y, and H to the current position.
     *
     * @param deltaX: difference in x
     * @param deltaY: difference in y
     * @param deltaH: difference in h
     */
    fun addPos(deltaX: Double, deltaY: Double, deltaH: Double){
        x += deltaX
        y += deltaY
        h += deltaH
    }

    /**
     * Logs the current X, Y, and H to the Position CSV file. Call at the very end of Auto.
     * We need this because at the end of autonomous, the program has to terminate and in teleOP, starts again.
     * This resets all variables in the program, meaning that we have to log the variables at the end of Autonomous
     * so we can read them in the begining of teleOp.
     */
    fun logPos(){
        //Write the file
        CsvLogging.writeFile("Position", arrayOf(getPos()[0], getPos()[1], getPos()[2]))
        //Flush
        CsvLogging.flush("Position")
        //Close writer
        CsvLogging.close()
    }


    /**
     * Reads the Position file, and sets the current position accordingly.
     * Use at the begining of teleOP
     */
    fun readPositionFile(){
        //Turn the first line of the CSV into a string
        val line = File("Position.txt").bufferedReader().use { it.readLine() }
        //Make a regex(regular expression), this will split the line into useable double values
        val regex = "[,]"

        //Split the line into a String List, x located at [0], y at [1], h at [2]
        val arrPos = line.split(regex)

        //Set the position
        setPos(line[0] as Double, line[1] as Double, line[2] as Double)
    }

    /**
     * This will begin tracking in TeleOp. Continuously logs position to x, y and h
     */
    fun updatePos(){
        addPos(tracker.position.x, tracker.position.y, tracker.position.h)
    }



}