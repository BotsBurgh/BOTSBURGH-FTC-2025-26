package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "autoFarBlue(6 ball)")
class autoFarBlue6: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)

        waitForStart()

        Turret.launch(-0.865)
        Turret.moveToTick(375)
        sleep(3000)

        //fire
        TransferSystem.setTransferPwr(-1.0)
        sleep(3000)
        TransferSystem.setTransferPwr(0.0)

        TransferSystem.setIntakePwr(-1.0)

        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 50.0, 0.0, 3.0)
        SpecterDrive.path(0.0, -12.0, 0.0)
        SpecterDrive.path(-10.0, 0.0, 0.0)
        SpecterDrive.path(0.0, 14.0, 0.0, 1.0)
        SpecterDrive.path(0.0, -50.0, 0.0, 3.0)

        //fire
        Turret.moveToTick(-335)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(3000)
        TransferSystem.setTransferPwr(0.0)
    }
}