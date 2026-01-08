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

    lateinit var Transfer : DcMotorEx
        private set
    lateinit var intake : DcMotorEx
        private set

    override fun init(opMode: OpMode) {
        super.init(opMode)

        Transfer = this.opMode.hardwareMap.get(DcMotorEx::class.java, "Transfer")
        intake = this.opMode.hardwareMap.get(DcMotorEx::class.java, "intake")
    }

    fun setIntakePwr(iP : Double){
        intake.power = iP
    }

      fun setTransferPwr(power: Double) {
          Transfer.power = power

      }

}