package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.abs

object Limelight : API() {

    private lateinit var cam: Limelight3A
    private var targetId: Int = -1



    // Variables for Vision
    var area = 0.0     //DistanceCalculation
        private set
    var angleX = 0.0    // TurretTracking
        private set
    var seesTag = false
        private set

    //PID constants
    private val Kp = 0.01
    private val Ki = 0.0
    private val Kd = 0.0

    //deadband is so the turret doesn't jitter
    private val deadband = 1.0

    //PID memory
    private var lastError = 0.0
    private var integralSum = 0.0
    private var lastTime = System.currentTimeMillis()

    fun init(opMode: OpMode, pipeline: Int) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")
        cam.pipelineSwitch(pipeline) //tracks pipeline, switch to 1 to track red
        cam.start()
    }

    //run this in loop
    fun update() {
        val result = cam.latestResult

        if (result == null || !result.isValid) {
            seesTag = false
            angleX = 0.0
            area = 0.0
            return
        }

        val tag = if (targetId == -1) {
            result.fiducialResults.firstOrNull()
        } else {
            result.fiducialResults.find { it.fiducialId == targetId }
        }

        if (tag != null) {
            seesTag = true
            angleX = tag.targetXDegrees //tracks angle offset
            area = tag.targetArea   //tracks area
        } else {
            seesTag = false
            angleX = 0.0  //default
            area = 0.0
        }
    }

    //Auto Aiming Function
    fun getTurretPower(): Double {
        if (!seesTag) {
            //resets the stuff when lost
            integralSum = 0.0
            lastError = 0.0
            lastTime = System.currentTimeMillis()
            return 0.0
        }

        val currentTime = System.currentTimeMillis()
        val deltaTime = (currentTime - lastTime) / 1000.0
        if (deltaTime <= 0.0) return 0.0

        val error = angleX  // degrees off center

        if (abs(error) < deadband) {
            integralSum = 0.0
            lastError = error
            lastTime = currentTime
            return 0.0
        }
        //porpotional
        val P = error * Kp
        //integral
        integralSum += error * deltaTime
        integralSum = integralSum.coerceIn(-0.2, 0.2)
        val I = integralSum * Ki
        //derivatice
        val derivative = (error - lastError) / deltaTime
        val D = derivative * Kd

        lastError = error
        lastTime = currentTime

        val output = (P + I + D).coerceIn(-1.0, 1.0)

        //adds them
        return output
    }

}