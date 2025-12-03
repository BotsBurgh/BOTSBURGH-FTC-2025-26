package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt


@TeleOp(name = "TeleOpMain")

class teleOpMain : OpMode() {

    var initPos = DoubleArray(3)

    override fun init() {
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)

        initPos = RobotTracker.getPos(false)
    }

    override fun loop() {
        // joystick(Movement) input
        val joyX = -this.gamepad1.left_stick_x.toDouble()
        val joyY = -this.gamepad1.left_stick_y.toDouble()


        // PI / 3 because 0 radians is right, not forward
        val joyRadians = atan2(joyY, joyX) - (PI / 3.0) - (2.0 * PI / 3.0)

        val joyMagnitude = sqrt(joyY * joyY + joyX * joyX)

        val rotationPower = this.gamepad1.right_stick_x.toDouble()


        // movement of all wheels
        TriWheels.drive(
            joyRadians,
            joyMagnitude * RobotConfig.TeleOpMain.DRIVE_SPEED,
            rotation = rotationPower * RobotConfig.TeleOpMain.ROTATE_SPEED,
        )

        TransferSystem.setIntakePwr(0.5)

        TransferSystem.power(0.0, 0.0)

        Turret.trackPos(initPos, RobotTracker.getPos(false))

        Turret.stop()

        //buttons

        if (gamepad1.left_bumper){
            TransferSystem.power(1.0, 1.0, 1.0)
        }

        if (gamepad1.b){
            Turret.launch(RobotTracker.getPos(false))
        }

        if (gamepad1.a){
            TransferSystem.pusherUp()
            TransferSystem.pusherDown()
        }

        if(gamepad1.right_bumper){
            TransferSystem.power(-1.0, -1.0)
        }

        if(gamepad1.left_bumper){
            TransferSystem.setIntakePwr(-1.0)
        }

    }

}