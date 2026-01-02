package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.*
import kotlin.math.*

@TeleOp(name = "TeleOpMAINPLEAASESPEED")
class teleOpMain : OpMode() {
//position
    private var initPos = DoubleArray(3)
    private var turretOn = false
    private var lastCircle = false

    override fun init() {
        //initing EVERYTHING
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)
        Limelight.init(this)

        initPos = RobotTracker.getPos(false)


    }

    override fun loop() {

        Limelight.update()
        val currentDeltaPos = RobotTracker.getPos(false)


        Turret.setTargetPos(RobotTracker.readPositionFile()[3], RobotTracker.readPositionFile()[4])
        Turret.trackPos(initPos, currentDeltaPos)

        val joyX = -this.gamepad1.left_stick_x.toDouble()
        val joyY = -this.gamepad1.left_stick_y.toDouble()

        val joyRadians = atan2(joyY, joyX) - (PI / 3.0) - (2.0 * PI / 3.0)
        val joyMagnitude = sqrt(joyY * joyY + joyX * joyX)
        val rotationPower = -this.gamepad1.right_stick_x.toDouble()

        TriWheels.drive(
            joyRadians,
            joyMagnitude * RobotConfig.TeleOpMain.DRIVE_SPEED,
            rotationPower * RobotConfig.TeleOpMain.ROTATE_SPEED
        )

        if (gamepad1.left_bumper) {
            TransferSystem.power(1.0, -1.0, 1.0)
        } else {
            if (gamepad1.right_trigger > 0.0) {
                TransferSystem.power(-1.0, 1.0)
            } else {
                TransferSystem.power(0.0, 0.0)
            }

            if (gamepad1.left_trigger > 0.0) {
                TransferSystem.setIntakePwr(-1.0)
            } else {
                TransferSystem.setIntakePwr(0.5)
            }
        }


        if (gamepad1.circle && !lastCircle) {
            turretOn = !turretOn
            if (turretOn) Turret.launch(currentDeltaPos) else Turret.stop()
        }
        lastCircle = gamepad1.circle

        if (gamepad1.cross) TransferSystem.pusherUp()
        if (gamepad1.triangle) TransferSystem.pusherDown()


    }
}