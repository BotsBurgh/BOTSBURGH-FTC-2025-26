package org.firstinspires.ftc.teamcode.api

import android.view.Gravity
import org.firstinspires.ftc.teamcode.RobotConfig.Turret
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
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
    lateinit var aimer: DcMotorEx
        private set

    /**
     * The motors that launch the ball
     */
    lateinit var launcherL: DcMotorEx
        private set
    lateinit var launcherR: DcMotorEx
        private set

    /**
     * The servo hood
     */
    lateinit var hood: Servo
        private set

    /**
     * The light, for some reason its programmed as a servo
     */
    lateinit var light: Servo

    var targetPos = doubleArrayOf()

    override fun init(opMode: OpMode) {
        super.init(opMode)

        aimer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "aimer")
        launcherL = this.opMode.hardwareMap.get(DcMotorEx::class.java, "launcherL")
        launcherR = this.opMode.hardwareMap.get(DcMotorEx::class.java, "launcherR")
        hood = this.opMode.hardwareMap.get(Servo::class.java, "hood")
        light = this.opMode.hardwareMap.get(Servo::class.java,"light")


        aimer.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcherL.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcherR.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
        launcherL.mode = DcMotor.RunMode.RUN_USING_ENCODER
        launcherR.mode = DcMotor.RunMode.RUN_USING_ENCODER
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
     */
    fun trackPos(startPos: DoubleArray, deltaPos: DoubleArray) {

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

        val turretPos = (motorTicks).toInt()


        //move turret
        aimer.targetPosition = turretPos
        aimer.mode = DcMotor.RunMode.RUN_TO_POSITION
        aimer.power = 0.5
    }

    /**
     * Fires the ball with a given power
     * @param power the power the motor is given to fire
     */
    fun launch(power: Double) {
        launcherL.power = power
        launcherR.power = power
    }

    /**
     * Stops the launcher
     */
    fun stop() {
        launcherL.power = 0.0
        launcherR.power = 0.0
    }


    /**
     * Moves turret to a certain tick
     */
    fun moveToTick(tick: Int){
        aimer.targetPosition = tick
        aimer.mode = DcMotor.RunMode.RUN_TO_POSITION
        while (aimer.currentPosition != aimer.targetPosition) {
            aimer.power = 0.5
        }
    }

    /**
     * Moves aimer with a certain power
     * @param power the power the motor moves
     */
    fun setAimerPower(power: Double){
        aimer.power = power
    }

    /**
     * Locks servo
     */
    fun lockServo(){
        hood.position =0.0
    }

    /**
     * Power the light
     */
    fun light(ligma : Double){
        light.position = ligma
    }

}



