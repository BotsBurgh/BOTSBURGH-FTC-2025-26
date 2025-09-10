package org.firstinspires.ftc.teamcode.teleop.demos

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.api.demos.DemoAPI

@TeleOp(name = "Demo TeleOp", group = "Demo")
@Disabled
class DemoTeleOp : OpMode() {
    // Run once, when "Init" is pressed on driver hub.
    override fun init() {
        // Initializes an API, shared code used by components.
        DemoAPI.init(this)

        // This line will throw an exception because linear APIs cannot be initialized with OpModes,
        // only LinearOpModes:
        // DemoLinearAPI.init(this)
    }

    // Run repeatedly while the robot is running.
    override fun loop() {
        val x = this.gamepad1.left_stick_x.toDouble()
        val y = this.gamepad1.left_stick_y.toDouble()
        val rotation = this.gamepad1.right_stick_x.toDouble()

        // Strafe + rotate robot using left and right joysticks.
        DemoAPI.drive(
            x - y + rotation,
            -x - y - rotation,
            -x - y + rotation,
            x - y - rotation,
        )
    }
}
