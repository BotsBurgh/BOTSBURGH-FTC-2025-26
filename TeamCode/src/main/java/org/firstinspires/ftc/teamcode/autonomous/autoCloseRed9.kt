package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "autoCloseRed (9 ball)")
class autoCloseRed9: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)


        waitForStart()
        Turret.lockServo()

        TransferSystem.setIntakePwr(-1.0)
        Turret.launch(-0.63)
        SpecterDrive.linearPath(0.0, -24.0, 0.0)
        sleep(100)
        SpecterDrive.linearPath(36.0, 0.0, 0.0)
        Turret.moveToTick(-264)

        //fire
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(-1.0)
        sleep(3500)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(-1.0)
        //eofire

        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 35.0, 0.0, 1.5)
        sleep(100)
        TransferSystem.setTransferPwr(0.0)
        SpecterDrive.path(0.0, -35.0, 0.0, 1.5)


        //fire
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(-1.0)
        sleep(3000)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(-1.0)
        //eofire


        SpecterDrive.linearPath(22.5, 0.0, 0.0)
        sleep(100)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 42.0, 0.0, 2.5)
        TransferSystem.setTransferPwr(0.0)
        sleep(100)
        SpecterDrive.path(0.0, -42.0, 0.0, 2.5)
        sleep(100)
        SpecterDrive.linearPath(-24.0, 0.0, 0.0)

        //fire
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(-1.0)
        sleep(3000)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(-1.0)
        //eofire

        Turret.moveToTick(0)

        SpecterDrive.linearPath(43.0, 0.0, 0.0)
        sleep(100)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 45.0, 0.0, 2.5)
        sleep(100)
        SpecterDrive.path(0.0, -45.0, 0.0, 2.5)

    }
}