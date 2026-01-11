package org.firstinspires.ftc.teamcode.api
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.core.API
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.RobotConfig


/**
 * An API to control the intake and transfer to turret
 */
object TransferSystem : API(){
    lateinit var intake : DcMotorEx
        private set
    lateinit var transfer: DcMotorEx
        private set


    override fun init(opMode: OpMode) {
        super.init(opMode)
        intake = this.opMode.hardwareMap.get(DcMotorEx::class.java, "intake")
        transfer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "transfer")

    }

    fun setIntakePwr(iP : Double){
        intake.power = iP
    }

    fun setTransferPwr(iP: Double){
        transfer.power = iP
    }

}