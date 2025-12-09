package org.firstinspires.ftc.teamcode.api

import android.view.Gravity
import org.firstinspires.ftc.teamcode.RobotConfig.Turret
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.atan2
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.normalizeDegrees

/**
 * An API to control the turret
 */
object Turret : API() {

    /**
     * The motor that aims the turret
     */
    lateinit var aimer : DcMotorEx
        private set

    /**
     * The motor that launches the ball
     */
    lateinit var launcher : DcMotorEx
        private set

    var targetPos = doubleArrayOf()

    override fun init(opMode: OpMode) {
        super.init(opMode)

        aimer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "aimer")
        launcher = this.opMode.hardwareMap.get(DcMotorEx::class.java, "launcher")

        aimer.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcher.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
        launcher.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    /**
     * Finds the starting position of the robot in Autonomous.
     * @param startSide the side the robot starts on (Red/Blue -> 0.0/1.0)
     * @param startPos  is the robot close to the obelisk (Yes/No -> 0.0/1.0)
     */
    fun setTargetPos(startSide: Double, startPos: Double) {
        //TODO: Magic Number system SUCKS, but csv only logs doubles for now, so we have to until we can log strings and use enums
        targetPos = when {
            startSide == 0.0 && startPos == 0.0 -> Turret.CLOSE_RED_TO_BASKET
            startSide == 1.0 && startPos == 0.0 -> Turret.CLOSE_BLUE_TO_BASKET
            startSide == 0.0 && startPos == 1.0 -> Turret.FAR_RED_TO_BASKET
            startSide == 1.0 && startPos == 1.0 -> Turret.FAR_BLUE_TO_BASKET
            else -> doubleArrayOf(0.0, 0.0)
        }
    }


    /**
     * Constantly makes sure that the turret is facing the basket.
     * @param startPos the starting position of the robot
     * @param deltaPos the change in position of the robot
     *
     * Returns angle
     */
    fun trackPos(startPos: DoubleArray, deltaPos: DoubleArray): Double {

        //robot’s current  position
        val robotX = startPos[0] + deltaPos[0]
        val robotY = startPos[1] + deltaPos[1]

        //direction vector to target
        val dx = targetPos[0] - robotX
        val dy = targetPos[1] - robotY

        //angle
        val theta = normalizeDegrees(Math.toDegrees(atan2(dy, dx)) + deltaPos[2])

        //convert output angle to motor ticks
        val motorTicks = theta * Turret.TICKS_PER_DEGREE

        val turretPos = (motorTicks * 2.14).toInt()

        //move turret
        aimer.targetPosition = turretPos
        aimer.mode = DcMotor.RunMode.RUN_TO_POSITION
        aimer.power = 0.5

        return theta   
    }

    /**
     * Calculates and fires the needed velocity for the ball to reach the basket
     * @param position the current position of the robot
     */
    fun launch(positon: DoubleArray) {

        launcher.power = 1.0

        //var mag = Math.sqrt(positon[0] * targetPos[0] + positon[1] * targetPos[1] + 47 * 47)

        //launcher.setVelocity((35.6 * mag * Math.sqrt((1/(26+0.9*mag)))) / Turret.GEAR_RATIO_LAUNCHER * Turret.TICKS_PER_RADIANS)

    }

    /**
     * Stops the launcher
     */
    fun stop() {
        launcher.power = 0.0
        aimer.power = 0.0
    }
    fun aimAtTag() {
        val kP = 0.015

        if (!Limelight.seesTag()) {
            // Tag lost — hold position
            aimer.power = 0.0
            return
        }

        // Tag visible — aim
        val error = Limelight.latestTx

        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
        aimer.power = error * kP
    }

}



