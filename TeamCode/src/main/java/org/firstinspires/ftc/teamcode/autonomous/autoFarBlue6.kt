package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive.otos


@Autonomous(name = "FAR BLUE")
class autoFarBlue6: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(60.0, 8.0, 270.0, true)


        waitForStart()

        //Launch 3 balls
        Turret.changeTargetVelocity(140.0)
        Turret.moveToTick(396)
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2350)
        Turret.launch(0.5)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Move to Lower 3 balls
        SpecterDrive.path(20.0, 0.0, 0.0, 2.0)
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 60.0, 0.0, 2.75)
        TransferSystem.setTransferPwr(-0.4)
        TransferSystem.setIntakePwr(0.0)
        SpecterDrive.path(0.0, -42.0, 0.0, 2.5)
        TransferSystem.setTransferPwr(-0.4)
        SpecterDrive.path(-25.0, -0.0, 0.0, 1.0)

        //FIRE
        Turret.moveToTick(420)
        Turret.launch()
        sleep(1900)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2350)
        Turret.stop()
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Leave
        SpecterDrive.path(30.0, 0.0, 0.0)



        //Singleton logging
        var finX = otos.position.x
        var finY = otos.position.y
        var finH = otos.position.h

        Singleton.finalXInches = finX
        Singleton.finalYInches = finY
        Singleton.finalHeadingDeg = finH

        Singleton.team = "Blue"
        Singleton.starting = "Far"
        Singleton.tagTracking = 0
        Singleton.autoRan = true
        Turret.moveToTick(0)


    }
}