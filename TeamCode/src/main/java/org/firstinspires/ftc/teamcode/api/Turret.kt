package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.atan2
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.normalizeDegrees

object Turret : API() {

    lateinit var aimer: DcMotorEx
    lateinit var launcher: DcMotorEx

    var targetPos = doubleArrayOf(0.0, 0.0)

    override fun init(opMode: OpMode) {
        aimer = opMode.hardwareMap.get(DcMotorEx::class.java, "aimer")
        launcher = opMode.hardwareMap.get(DcMotorEx::class.java, "launcher")

        aimer.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcher.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
        launcher.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    
    fun trackPos(startPos: DoubleArray, deltaPos: DoubleArray): Double {

        if (Limelight.seesTag) {
            trackTag(Limelight.angleX)
            return Limelight.angleX
        }

        val robotX = startPos[0] + deltaPos[0]
        val robotY = startPos[1] + deltaPos[1]
        val dx = targetPos[0] - robotX
        val dy = targetPos[1] - robotY

        val theta = normalizeDegrees(Math.toDegrees(atan2(dy, dx)) + deltaPos[2])
        val motorTicks = (theta * RobotConfig.Turret.TICKS_PER_DEGREE * 2.14).toInt()

        aimer.targetPosition = motorTicks
        aimer.mode = DcMotor.RunMode.RUN_TO_POSITION
        aimer.power = 0.5

        return theta
    }

    /**
     * Limelight-specific proportional control
     */
     fun trackTag(angleX: Double) {
        val Kp = 0.04
        val minPower = 0.05
        val deadband = 0.5

        if (Math.abs(angleX) > deadband) {
            val power = (angleX * Kp) + (Math.signum(angleX) * minPower)
            aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
            aimer.power = power.coerceIn(-0.5, 0.5)
        } else {
            aimer.power = 0.0
        }
    }

    fun setTargetPos(startSide: Double, startPos: Double) {
        targetPos = when {
            startSide == 0.0 && startPos == 0.0 -> RobotConfig.Turret.CLOSE_RED_TO_BASKET
            startSide == 1.0 && startPos == 0.0 -> RobotConfig.Turret.CLOSE_BLUE_TO_BASKET
            startSide == 0.0 && startPos == 1.0 -> RobotConfig.Turret.FAR_RED_TO_BASKET
            startSide == 1.0 && startPos == 1.0 -> RobotConfig.Turret.FAR_BLUE_TO_BASKET
            else -> doubleArrayOf(0.0, 0.0)
        }
    }

    fun launch(position: DoubleArray) {
        launcher.power = 1.0
    }

    fun stop() {
        launcher.power = 0.0
        aimer.power = 0.0
    }
}