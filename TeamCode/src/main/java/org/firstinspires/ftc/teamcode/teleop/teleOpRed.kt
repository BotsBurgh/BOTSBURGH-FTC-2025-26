package org.firstinspires.ftc.teamcode.teleop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.Limelight
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.Voltage
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt


@TeleOp(name = "teleOpRed")

class teleOpRed : OpMode() {

    var initPos = DoubleArray(3)
    var turretOn = false
    var lastCircle = false

    var crossPressed = false

    var launchPwr = 1.0

    override fun init() {
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)
        Voltage.init(this)
        Limelight.init(this, 0)

        initPos = doubleArrayOf(RobotTracker.readPositionFile()[0], RobotTracker.readPositionFile()[1], RobotTracker.readPositionFile()[2])
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

        val rotationPower = 0 -this.gamepad1.right_stick_x.toDouble()

        // movement of all wheels
        TriWheels.drive(
            joyRadians,
            joyMagnitude * RobotConfig.TeleOpMain.DRIVE_SPEED,
            rotation = rotationPower * RobotConfig.TeleOpMain.ROTATE_SPEED,
        )

        Turret.setTargetPos(0.0, 1.0)

        //Turret.trackPos(initPos, RobotTracker.getPos(false))

        //limelight
        if (Limelight.seesTag && gamepad2.left_stick_x.toDouble() == 0.0) {
            val power = Limelight.getTurretPower()
            Turret.setAimerPower(power)
        } else {
            Turret.setAimerPower(gamepad2.left_stick_x.toDouble())
        }

        //Lock turret
        Turret.lockServo()


        //buttons

        //Toggle launch power between 1, 0.9, and 0.75
        if (gamepad2.a) {
            if (!crossPressed) {
                launchPwr = when (launchPwr) {
                    1.0 -> 0.9
                    0.9 -> 0.8
                    0.8 -> 0.7
                    0.7 -> 0.6
                    0.6 -> 0.5
                    0.5 -> 0.4
                    else -> 1.0
                }
                crossPressed = true
            }
        } else {
            crossPressed = false
        }

        //Light
        when (launchPwr) {
            1.0 -> {
                Turret.light(0.722) //green
            }
            0.9 -> {
                Turret.light(0.666)
            }
            0.8 -> {
                Turret.light(0.611) //yellow
            }
            0.7 -> {
                Turret.light(0.500)
            }
            0.6 -> {
                Turret.light(0.388) //red
            }
            0.5 -> {
                Turret.light(0.333)
            }
            else -> {
                Turret.light(0.277)

            }
        }

        //Toggle turret on and off
        if (gamepad2.b && !lastCircle) {
            turretOn = !turretOn

            if (turretOn) {
                Turret.launch(-launchPwr)
            }   // turn on
            else {
                Turret.stop()
            }
        }

        lastCircle = gamepad2.b


        if (gamepad1.left_bumper) {
            TransferSystem.setIntakePwr(-1.0)
        }

        if (gamepad1.left_trigger > 0.0) {
            TransferSystem.setIntakePwr(1.0)
        }

        if (gamepad1.right_bumper) {
            TransferSystem.setTransferPwr(-1.0)
        }

        if (gamepad1.right_trigger > 0.0) {
            TransferSystem.setTransferPwr(1.0)
        }

        if (!gamepad1.left_bumper && gamepad1.left_trigger.toDouble() == 0.0) {
            TransferSystem.setIntakePwr(-0.4)
        }

        if (!gamepad1.right_bumper && gamepad1.right_trigger.toDouble() == 0.0){
            TransferSystem.setTransferPwr(0.0)
        }




        telemetry.addData("Tick", Turret.aimer.currentPosition)
        telemetry.addData("Power", launchPwr)

    }

}