package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "autoFarBlue")
class autoFarBlue: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        RobotTracker.init(this)
        TransferSystem.init(this)

        waitForStart()
        Turret.setTargetPos(0.0, 1.0)
        Turret.launch(RobotTracker.getPos(true))
        TransferSystem.setIntakePwr(0.5)

        SpecterDrive.path(0.09, 6.31, 0.0, 0.0)
        sleep(100)

        SpecterDrive.rotate(45.0)
        TransferSystem.power(1.0, -1.0)
        TransferSystem.pusherUp()
        sleep(50)
        TransferSystem.pusherDown()

        SpecterDrive.rotate(45.0)

        SpecterDrive.path(-2.04, -4.10, 0.0, 0.0)
        sleep(100)

        TransferSystem.setIntakePwr(1.0)
        RobotConfig.OTOS.SPEED = 0.05

        SpecterDrive.path(-1.57, -0.03, 0.0, 0.0)
        sleep(100)

        TransferSystem.setIntakePwr(0.5)
        SpecterDrive.rotate(315.0)
        RobotConfig.OTOS.SPEED = 0.5

        SpecterDrive.path(3.63, 4.09, 0.0, 0.0)
        sleep(100)
        for (i in 0..2) {
            TransferSystem.power(1.0, -1.0)
            TransferSystem.pusherUp()
            sleep(50)
            TransferSystem.pusherDown()
        }
        RobotTracker.logPos(1.0, 1.0)

    }
}