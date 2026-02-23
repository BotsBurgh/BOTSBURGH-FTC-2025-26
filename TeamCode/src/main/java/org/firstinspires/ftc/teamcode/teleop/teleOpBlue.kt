package org.firstinspires.ftc.teamcode.teleop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.api.Limelight
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.Voltage
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt


@TeleOp(name = "teleOpBlue")

class teleOpBlue : OpMode() {

    var turretOn = false
    var lastCircle = false

    override fun init() {
        //Scan for Singleton
        TriWheels.init(this)
        RobotTracker.teleInit(this)
        Turret.init(this)
        TransferSystem.init(this)
        Voltage.init(this)
        Limelight.init(this, 3)

        telemetry.addData("cX", RobotTracker.getPos(true)[0])
        telemetry.addData("cY", RobotTracker.getPos(true)[1])
        telemetry.addData("cH", RobotTracker.getPos(true)[2])
        telemetry.addData("sX", Singleton.finalXInches)
        telemetry.addData("sY", Singleton.finalXInches)
        telemetry.addData("sH", Singleton.finalXInches)

    }

    override fun loop() {
        //updates first
        Limelight.update(Turret.aimer.currentPosition)
        Turret.changeTargetVelocity(sqrt((RobotConfig.UniversalCoordinates.BLUE_POS[0]- RobotTracker.getPos(false)[0]).squared()+(RobotConfig.UniversalCoordinates.BLUE_POS[1]- RobotTracker.getPos(false)[1]).squared()))

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
        if (abs(gamepad2.left_stick_x/15) > 0.05) {
            // Manual override
            Turret.setAimerPower(gamepad2.left_stick_x.toDouble())
        }
        else if (Limelight.seesTag) {
            Turret.setAimerPower(Turret.getTurretPower())
        }
        else {
            Turret.setAimerPower(0.0)
        }


        if(!Limelight.seesTag){
            Turret.light(0.28)
        }
        else {
            Turret.light(0.5)
        }

        //Toggle turret on and off
        if (gamepad2.a){
            Turret.launch(0.1)
        }
        else if (gamepad2.circle && !lastCircle) {
            turretOn = !turretOn

            if (turretOn) {
                Turret.launch()
                gamepad2.rumble(250)
            }
            // turn on
            else {
                Turret.stop()
            }
        }
        else {
            Turret.stop()

        }

        lastCircle = gamepad2.circle

        //Update turret speed
        if (turretOn) {
            Turret.launch()
            gamepad2.rumble(250)
        }


//        //rumble to show velocity
        val veloEr = abs(Turret.launcherL.velocity + Turret.TARGET_VELOCITY)
        val shootReady = veloEr < 50
//
        if(turretOn && shootReady){
            Turret.light2(0.5)


        }
        else{
            Turret.light2(0.28)
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

        if(gamepad2.left_trigger > 0.0 && gamepad2.right_trigger > 0.0){
            RobotTracker.setPos(134.5, 9.5, 0.0, false)
        }

        telemetry.addData("Distance", sqrt((RobotConfig.UniversalCoordinates.BLUE_POS[0]- RobotTracker.getPos(false)[0]).squared()+(RobotConfig.UniversalCoordinates.BLUE_POS[1]- RobotTracker.getPos(false)[1]).squared()))
        telemetry.addData("Velocity", Turret.launcherL.velocity)
        telemetry.addData("Power", Turret.launcherL.power)

        telemetry.addData("X", RobotTracker.getPos(false)[0])
        telemetry.addData("Y", RobotTracker.getPos(false)[1])
        telemetry.addData("H", RobotTracker.getPos(false)[2])
        telemetry.addData("Tick", Turret.aimer.currentPosition)
    }
}