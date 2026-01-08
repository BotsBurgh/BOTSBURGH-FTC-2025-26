package org.firstinspires.ftc.teamcode.teleop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt
import com.qualcomm.robotcore.hardware.DcMotor



@TeleOp(name = "TeleopV2")

class teleOpMain : OpMode() {

    var initPos = DoubleArray(3)
    var turretOn = false
    var lastCircle = false

    override fun init() {
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)
        Limelight.init(this)

        initPos = RobotTracker.getPos(false)
    }

    override fun loop() {
        Limelight.update(Turret.aimer.currentPosition)
        telemetry.clear()
        // joystick(Movement) input

        val joyX = -this.gamepad1.left_stick_x.toDouble()
        val joyY = this.gamepad1.left_stick_y.toDouble()

        // PI / 3 because 0 radians is right, not forward


        val joyRadians = atan2(joyY, joyX) - (PI / 3.0) - (2.0 * PI / 3.0)

        val joyMagnitude = sqrt(joyY * joyY + joyX * joyX)

        val rotationPower = -this.gamepad1.right_stick_x.toDouble()
        // movement of all wheels


        TriWheels.drive(
            joyRadians,
            joyMagnitude * RobotConfig.TeleOpMain.DRIVE_SPEED,
            rotation = rotationPower * RobotConfig.TeleOpMain.ROTATE_SPEED,
        )

        //limelight
        if (Limelight.seesTag) {
            Turret.aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
            val power = Limelight.getTurretPower()
            Turret.setAimerPower(power)
        } else {
            Turret.setAimerPower(0.0)
        }


        //buttons


        if (gamepad1.circle && !lastCircle) {
            turretOn = !turretOn

            if (turretOn) {
                Turret.launch(RobotTracker.getPos(false))
            }   // turn on
            else {
                Turret.stop()
            }
        }

        lastCircle = gamepad1.circle




        if (gamepad1.left_bumper) {
            TransferSystem.setIntakePwr(1.0)
        }

        if (!gamepad1.left_bumper) {
            TransferSystem.setIntakePwr(0.0)
        }


        if (gamepad1.left_trigger > 0.0) {
            TransferSystem.setIntakePwr(-1.0)
        }

        if (gamepad1.right_bumper) {
            TransferSystem.setTransferPwr(1.0)
        }

        if (gamepad1.right_trigger > 0.0) {
            TransferSystem.setTransferPwr(-1.0)
        }


        if (!gamepad1.left_bumper && gamepad1.left_trigger.toDouble() == 0.0) {
            TransferSystem.setIntakePwr(-0.25)
        }
        telemetry.addData("Target Area (%)", "%.6f", 100*Limelight.area)
        telemetry.update()
    }

}