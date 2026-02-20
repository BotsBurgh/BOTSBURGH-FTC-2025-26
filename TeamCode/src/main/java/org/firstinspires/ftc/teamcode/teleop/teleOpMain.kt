package org.firstinspires.ftc.teamcode.teleop
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
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt


@TeleOp(name = "teleOpMain")

class teleOpMain : OpMode() {

    var turretOn = false
    var lastCircle = false

    var crossPressed = false

    var launchPwr = 1.0

    override fun init() {
        //Scan for Singleton
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)
        Voltage.init(this)
        //Warning in case singleton did not write correctly
        if(!Singleton.autoRan){
            telemetry.addLine("WARNING: AUTO DATA NOT FOUND")
            telemetry.addLine("PRESS DPAD BUTTONS ON GAMEPAD 2 TO CORRESPOND WITH STARTING AUTO POSITION")
            telemetry.addLine("UP > FAR RED")
            telemetry.addLine("LEFT > FAR BLUE")
            telemetry.addLine("RIGHT > CLOSE RED")
            telemetry.addLine("DOWN > CLOSE BLUE")

            //Far Red
            if(gamepad2.dpad_up){
                RobotTracker.setPos(0.0, 0.0, 0.0, false)
                Limelight.init(this, 1)
                telemetry.clear()
                telemetry.addLine("SELECTED: FAR RED")
            }

            //Far Blue
            else if(gamepad2.dpad_left){
                RobotTracker.setPos(0.0, 0.0, 0.0, false)
                Limelight.init(this, 0)
                telemetry.clear()
                telemetry.addLine("SELECTED: FAR BLUE")
            }

            //Close Red
            else if(gamepad2.dpad_right){
                RobotTracker.setPos(0.0, 0.0, 0.0, false)
                Limelight.init(this, 1)
                telemetry.clear()
                telemetry.addLine("SELECTED: CLOSE RED")
            }

            //Close Blue
            else if(gamepad2.dpad_left){
                RobotTracker.setPos(0.0, 0.0, 0.0, false)
                Limelight.init(this, 0)
                telemetry.clear()
                telemetry.addLine("SELECTED: CLOSE BLUE")
            }
        }

        //Transfer data for auto if found
        else {
            telemetry.addLine("AUTO DATA FOUND")
            telemetry.addLine("TEAM: "+ Singleton.team)
            telemetry.addLine("AUTO STARTING POS: "+ Singleton.starting)
            RobotTracker.setPos(Singleton.finalXInches, Singleton.finalYInches, Singleton.finalHeadingDeg, false)
            Limelight.init(this, Singleton.tagTracking)
        }
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

        //limelight tracking
        if (Limelight.seesTag) {
            val power = Turret.getTurretPower()
            Turret.setAimerPower(power)
        } else {
            Turret.setAimerPower(0.0)
        }

        //LL overrides
        Turret.setAimerPower(gamepad2.left_stick_x.toDouble())

        //LimeLight LED tracker
        if(!Limelight.seesTag){
            Turret.light(0.28)
        }
        else {
            Turret.light(0.5)
        }

        //Toggle turret on and off
        if (gamepad1.circle && !lastCircle) {
            turretOn = !turretOn

            if (turretOn) {
                Turret.launch()
            }   // turn on
            else {
                Turret.stop()
            }
        }

        lastCircle = gamepad1.circle

        //Update turret speed
        if (turretOn) {
            Turret.launch()
        }

        //rumble to show velocity
        val veloEr = abs(Turret.launcherL.velocity + Turret.TARGET_VELOCITY)
        val shootReady = veloEr < 50

        if(turretOn && shootReady){
            gamepad1.rumble(300)
        }

        //buttons
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

        telemetry.addData("Distance", sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(false)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(false)[1]).squared()))
        telemetry.addData("Velocity", Turret.launcherL.velocity)
        telemetry.addData("Power", Turret.launcherL.power)

        telemetry.addData("X", RobotTracker.getPos(false)[0])
        telemetry.addData("Y", RobotTracker.getPos(false)[1])
        telemetry.addData("H", RobotTracker.getPos(false)[2])
        telemetry.addData("Tick", Turret.aimer.currentPosition)
    }
}