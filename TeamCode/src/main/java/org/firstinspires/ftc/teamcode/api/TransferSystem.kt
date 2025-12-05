package org.firstinspires.ftc.teamcode.api
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.core.API
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo


/**
 * An API to control the intake and transfer to turret
 */
object TransferSystem : API(){

    lateinit var leftTransfer : DcMotorEx
        private set
    lateinit var rightTransfer : DcMotorEx
        private set
    lateinit var intake : DcMotorEx
        private set
    lateinit var pusher : Servo
        private set

    override fun init(opMode: OpMode) {
        super.init(opMode)

        leftTransfer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "leftTransfer")
        rightTransfer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "rightTransfer")
        intake = this.opMode.hardwareMap.get(DcMotorEx::class.java, "intake")
        pusher = this.opMode.hardwareMap.get(Servo::class.java, "pusher")
    }

    fun power(lP : Double, rP : Double, iP : Double){
        leftTransfer.power = lP
        rightTransfer.power = rP
        intake.power = iP
    }

    fun power(lP : Double, rP : Double){
        leftTransfer.power = lP
        rightTransfer.power = rP
    }

    fun setIntakePwr(iP : Double){
        intake.power = iP
    }

    fun pusherUp(){
        pusher.position = 1.0

    }

    fun pusherDown(){
        pusher.position = 0.0
    }
}