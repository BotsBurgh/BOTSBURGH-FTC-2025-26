package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.core.API

object Limelight : API() {
    private lateinit var cam: Limelight3A
    private var targetId: Int = -1

    var angleX = 0.0
        private set
    var seesTag = false
        private set
// increase kP until it tracks the turret properly.if it starts shaking, lower it
    private val Kp = 0.025
    private val deadband = 5.0
    //deadbands purpose is to make less jitter in the motor

    private val Ki = 0.02  // increase THis VERY VERY slowly to make sure nothing overshoots and vibrates
    private val Kd = 0.075   //change this if the turret doesn't full reach target(keep this very small)
    //memory variabls
    private var lastError = 0.0 //previous angle difference
    private var integralSum = 0.0 //error adding up
    private var lastTime = System.currentTimeMillis() //difference in time in MS(idk man)

    override fun init(opMode: OpMode) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")
        cam.pipelineSwitch(0)
        cam.start()
    }

    fun update() {
        val result = cam.latestResult
        if (result == null || !result.isValid) {
            seesTag = false
            return
        }

        val tag = if (targetId == -1) {
            result.fiducialResults.firstOrNull()
        } else {
            result.fiducialResults.find { it.fiducialId == targetId }
        }

        if (tag != null) {
            seesTag = true
            angleX = tag.targetXDegrees
        } else {
            seesTag = false
        }
    }

    /**
     * does very complex hard math to get angle to turret movement
     */
    fun veryCOMPLEXMATHAMATICS(): Double {
        if (!seesTag) {
            integralSum = 0.0 // resets the memory if it cant see tag :(
            return 0.0
        }
        val currentTime = System.currentTimeMillis() //time
        val deltaTime = (currentTime - lastTime) / 1000.0 //conversion to seconds to make life easier :)
        if (deltaTime <= 0) //just to maek sure no dividing by zero
            return 0.0

        val error = angleX //this is the anglex differnce which is angle the turret needs to turn
        if (Math.abs(error) < deadband) {
            return 0.0
        }
        val P = error * Kp  //porpotional term

        integralSum += error * deltaTime
        //capping the interval so it doesn't just start spinning
        integralSum = integralSum.coerceIn(-0.2, 0.2)
        //integral
        val I = integralSum * Ki
        //derivative (future error)
        val derivative = if (deltaTime > 0)(error - lastError)/deltaTime
        else 0.0
        //DERIRATIVE :)
        val D = derivative * Kd
        //error
        lastTime = currentTime   //setting up variables

        lastError = error

        //ADDING EVERYTHINGGGGG
        val totalPower = P + I + D
    //return the value, and making sure it fits within the limits(double btw)
        return totalPower.coerceIn(-0.6, 0.6)

    }
    fun changeTagID(){

    }
}