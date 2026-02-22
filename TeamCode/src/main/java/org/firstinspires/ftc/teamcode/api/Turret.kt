package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.abs


/**
 * An API to control the turret
 */
object Turret : API() {
    /**
     * Soft Limits
     */
    @JvmField
    var howMuch = 0.0


    @JvmField
    var increments = 1.0


    @JvmField
    var LEFT_LIMIT = -1000

    @JvmField
    var RIGHT_LIMIT = 1000

    /**
     * Variables for turret launcher
     */
    @JvmField
    var LAUNCHER_KF = 0.475

    @JvmField
    var LAUNCHER_KP = 250.67

    @JvmField
    var TARGET_VELOCITY = 1660.0

    /**
     * *variables for aimer PID
     */

    val VISION_KP = 0.015
    val VISION_KI = 0.0
    val VISION_KD = 0.000007
    private val deadband = 1.0
    private var lastError = 0.0
    private var integralSum = 0.0
    private var lastTime = System.currentTimeMillis()

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
     * The hood
     */
    lateinit var servo: Servo
        private set

    lateinit var encoder: AnalogInput
        private set


    /**
     * The lights
     */
    lateinit var light: Servo

    lateinit var light2: Servo

    override fun init(opMode: OpMode) {
        super.init(opMode)

        servo = this.opMode.hardwareMap.get(Servo::class.java, "hood")
        encoder = this.opMode.hardwareMap.get(AnalogInput::class.java, "encoder")

        val coeffs = PIDFCoefficients(LAUNCHER_KP, 0.0, 0.0, LAUNCHER_KF)

        aimer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "aimer")
        launcherL = this.opMode.hardwareMap.get(DcMotorEx::class.java, "launcherL")
        launcherR = this.opMode.hardwareMap.get(DcMotorEx::class.java, "launcherR")
        light = this.opMode.hardwareMap.get(Servo::class.java, "light")
        light2 =  this.opMode.hardwareMap.get(Servo::class.java, "light2")

        aimer.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcherL.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        launcherR.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER

        launcherL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coeffs)
        launcherR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coeffs)
    }

    /**
     * Constantly makes sure that the turret is facing the basket.
     */
    fun trackPos(){
        //no
    }

    /**
     * Fires the ball
     * @param power the power it fires at
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
     * Powers the aimer
     */
    fun setAimerPower(power: Double){
        aimer.power = power
    }

    /**
     * Moves the servo to a given position
     * @param pos the position
     */
    fun moveHood(pos: Double){
        servo.position = pos
    }

    /**
     * Locks the servo
     */
    fun lockServo(){
        moveHood(0.0)
    }

    /**
     * Turns the light a certain color given a PWM value
     * @param PWM the pwm value given
     */
    fun light(PWM: Double){
        light.position = PWM
    }

    fun light2(PWM: Double){
        light2.position = PWM
    }

    /**
     * Moves the turret to a certain tick
     */
    fun moveToTick(
        targetTick: Int
    ) {
        aimer.targetPosition = targetTick
        aimer.mode = DcMotor.RunMode.RUN_TO_POSITION
        aimer.power = abs(1.0)
    }

    /**
     * PID for Aiming
     */

    fun getTurretPower(): Double {
        aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
        val currentPos = aimer.currentPosition
        if (!Limelight.seesTag) {
            //resets the stuff when lost
            integralSum = 0.0
            lastError = 0.0
            lastTime = System.currentTimeMillis()
            return 0.0
        }

        val currentTime = System.currentTimeMillis()
        val deltaTime = (currentTime - lastTime) / 1000.0
        if (deltaTime <= 0.0) return 0.0

        val error = Limelight.angleX  // degrees off center

        if (abs(error) < deadband) {
            integralSum = 0.0
            lastError = error
            lastTime = currentTime
            return 0.0
        }
        //porpotional
        val P = error * VISION_KP
        //integral
        integralSum += error * deltaTime
        integralSum = integralSum.coerceIn(-0.2, 0.2)
        val I = integralSum * VISION_KI
        //derivatice
        val derivative = (error - lastError) / deltaTime
        val D = derivative * VISION_KD

        lastError = error
        lastTime = currentTime

        var output = (P + I + D).coerceIn(-1.0, 1.0)

        if (currentPos <= LEFT_LIMIT && output < 0) {
            output = 0.0
        }
        // If trying to move further Right than the limit
        if (currentPos >= RIGHT_LIMIT && output > 0) {
            output = 0.0
        }
        //
//        //adds them
        return output
    }

    fun launch() {

        launcherR.velocity = -TARGET_VELOCITY
        launcherL.velocity = -TARGET_VELOCITY
    }

    fun changeTargetVelocity(distance: Double){
        var dist = distance - 18
        if(dist < 55.0){
            TARGET_VELOCITY = 0.104167 * dist.squared() - 5.41667 * dist + 1040
            moveHood(0.86)
//            light2(0.28)
            if (Singleton.team == "blue"){
                Limelight.cam.pipelineSwitch(0)
            }
            else if (Singleton.team == "red"){
                Limelight.cam.pipelineSwitch(1)
            }
        }

        else if(67.0 < dist && dist < 115){
            TARGET_VELOCITY = 0.00000223265 * dist * dist * dist * dist + 0.00160751 * dist * dist * dist - 0.465213 * dist.squared() + 41.41204 * dist
            moveHood(0.6)
//            light2(0.388)
            if (Singleton.team == "blue"){
                Limelight.cam.pipelineSwitch(0)
            }
            else if (Singleton.team == "red"){
                Limelight.cam.pipelineSwitch(1)
            }
        }

        else{
            TARGET_VELOCITY = -0.00726448*distance*distance*distance+ 3.04573*distance.squared() - 419.56442*distance+20596.7275
           moveHood(0.3)
            if (Singleton.team == "blue"){
                Limelight.cam.pipelineSwitch(3)
            }
            else if (Singleton.team == "red"){
                Limelight.cam.pipelineSwitch(2)
            }

//            light2(0.5)
        }
    }
}

