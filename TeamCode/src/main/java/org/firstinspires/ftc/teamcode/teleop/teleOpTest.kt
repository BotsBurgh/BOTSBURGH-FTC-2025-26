package org.firstinspires.ftc.teamcode.teleop
import androidx.core.graphics.component2
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.normalizeDegrees
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.Limelight
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.Voltage
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.RobotConfig.Turret.pos
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt


@TeleOp(name = "teleOpTest")

class teleOpTest : OpMode() {

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
        Limelight.init(this, 1)

        RobotTracker.setPos(Singleton.finalXInches, Singleton.finalYInches, Singleton.finalHeadingDeg, false)

    }

    override fun loop() {
        //updates first
        Limelight.update(Turret.aimer.currentPosition)
        Turret.changeTargetVelocity(sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(false)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(false)[1]).squared()))
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

        //limelight
        if (Limelight.seesTag) {
            val power = Turret.getTurretPower()
            Turret.setAimerPower(power)
        } else {
            Turret.setAimerPower(0.0)
        }

        if(!Limelight.seesTag){
            Turret.light(0.28)
        }
        else if(Limelight.seesTag){
            Turret.light(0.5)
        }

        //Toggle turret on and off
        if (gamepad2.b && !lastCircle) {
            turretOn = !turretOn

            if (turretOn) {
                Turret.launch()
            }   // turn on
            else {
                Turret.stop()
            }
        }

        lastCircle = gamepad2.b

        //Toggle launch power between 1, 0.9, and 0.75
        if (gamepad2.a) {
            if (!crossPressed) {
                launchPwr = when (launchPwr) {
                    1.0 -> 0.95
                    0.95 -> 0.9
                    0.9 -> 0.85
                    0.85 -> 0.8
                    0.8 -> 0.75
                    0.75 -> 0.7
                    0.7 -> 0.65
                    0.65 -> 0.6
                    0.6 -> 0.55
                    0.55 -> 0.5
                    0.5 -> 0.45
                    0.45 -> 0.4
                    0.4 -> 0.35
                    0.35 -> 0.3
                    0.3 -> 0.25
                    0.25 -> 0.2
                    0.2 -> 0.15
                    0.15 -> 0.1
                    0.1 -> 0.5
                    0.5 -> 1.0
                    else -> 1.0
                }
                crossPressed = true
            }
        } else {
            crossPressed = false
        }


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


        val theta =
            normalizeDegrees(
                Math.toDegrees
                    (atan2(RobotConfig.UniversalCoordinates.RED_POS[1] - RobotTracker.getPos(false)[1], RobotConfig.UniversalCoordinates.RED_POS[0] -  RobotTracker.getPos(false)[0]))
                        - RobotTracker.getPos(false)[2])

        telemetry.addData("theta", theta)

        telemetry.addData("Distance", sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(false)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(false)[1]).squared()))
        telemetry.addData("Velocity", Turret.launcherL.velocity)
        telemetry.addData("Power", Turret.launcherL.power)


        telemetry.addData("X", RobotTracker.getPos(false)[0])
        telemetry.addData("Y", RobotTracker.getPos(false)[1])
        telemetry.addData("H", RobotTracker.getPos(false)[2])
        telemetry.addData("Tick", Turret.aimer.currentPosition)
    }
//small 0.86, medium 0.6, far = 0.3
}