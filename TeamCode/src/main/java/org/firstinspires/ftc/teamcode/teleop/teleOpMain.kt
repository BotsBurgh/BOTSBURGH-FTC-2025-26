package org.firstinspires.ftc.teamcode.teleop
import org.firstinspires.ftc.teamcode.api.Limelight
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
        Limelight.init(this)

        initPos = RobotTracker.getPos(false)
    }

    override fun loop() {
        telemetry.clear()
        // joystick(Movement) input
        val joyX = -this.gamepad1.left_stick_x.toDouble()
        val joyY = -this.gamepad1.left_stick_y.toDouble()
        Limelight.update()
        Turret.aimAtTag()
        Limelight.targetTagId = 21







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

        Turret.setTargetPos(RobotTracker.readPositionFile()[3], RobotTracker.readPositionFile()[4])

        Turret.trackPos(initPos, RobotTracker.getPos(false))

        //buttons

        if (gamepad1.left_bumper){
            TransferSystem.power(1.0, -1.0, 1.0)
        }

        if (gamepad1.circle){
            Turret.launch(RobotTracker.getPos(false))

        }

        if (gamepad1.cross){
            TransferSystem.pusherUp()

        }

        if(gamepad1.right_trigger > 0.0){
            TransferSystem.power(-1.0, 1.0)
        }

        if(gamepad1.left_trigger > 0.0){
            TransferSystem.setIntakePwr(-1.0)
        }

        if(gamepad1.triangle){
            TransferSystem.pusherDown()
        }

        if(!gamepad1.left_bumper && gamepad1.right_trigger.toDouble() == 0.0){
            TransferSystem.power(0.0, 0.0)
        }

        if(!gamepad1.left_bumper && gamepad1.left_trigger.toDouble() == 0.0){
            TransferSystem.setIntakePwr(0.5)
        }

        if(!gamepad1.circle){
            Turret.stop()
        }


        telemetry.update()

    }

}