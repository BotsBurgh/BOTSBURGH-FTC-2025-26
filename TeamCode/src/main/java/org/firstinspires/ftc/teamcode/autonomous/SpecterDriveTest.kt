package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "SpecterDriveTest", group = "Test")

class SpecterDriveTest: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)

        waitForStart()

    }
}