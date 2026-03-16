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
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.sqrt

@Autonomous(name = "CLOSE BLUE")
class autoCloseBlue9: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(16.0, 120.0, 325.0, true)


        //Start Position: Angled against the wall, midpoint
        waitForStart()
        Turret.moveToTick(50)

        //Move back and charge turret
        Turret.changeTargetVelocity(90.0, true)
        Turret.launch()
        TransferSystem.setIntakePwr(-1.0)
        SpecterDrive.path(0.0, -56.0, 0.0, 3.0)

        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2000)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch(0.5)

        //Rotate to 90
        SpecterDrive.rotateToHeading(360.0, 0.7)

        //Forward and intake first 3 balls
        Turret.moveToTick(275) //FIND TICK
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (4.5*12.0)+3, 0.0, 2.5)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        sleep(500)
        Turret.launch()
        SpecterDrive.path(0.0, -(4.5*12.0)-5, 0.0, 1.6)
        Turret.changeTargetVelocity(80.0, true)


        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch(0.5)

        //Down
        SpecterDrive.path(-33.5, 0.0, 0.0, 2.0)
        SpecterDrive.rotateToHeading(365.5, 0.3)

        //Forward and intake next 3 balls, hit classifier
        Turret.stop()
        Turret.moveToTick(210)
        Turret.changeTargetVelocity(66.9, true)
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (5.5*12.0), 0.0, 2.75)
        TransferSystem.intake()
        TransferSystem.reverseTransfer()
        SpecterDrive.path(0.0, -12.0, 0.0, 2.0)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        SpecterDrive.path(12.0, 0.0, 0.0, 2.0)
        SpecterDrive.path(0.0, 18.0, 0.0, 1.0)
        Turret.launch()
        SpecterDrive.path(0.0, -(5.5*12.0)-6.7+4.0, 0.0, 1.0)

        //Up
        SpecterDrive.path(30.0, 0.0, 0.0, 1.5)
        SpecterDrive.path(15.0, 0.0, 0.0, 1.5)

        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Up
        SpecterDrive.path(0.0, -15.0, 0.0, 1.5)



        //Singleton logging
        var finX = otos.position.x
        var finY = otos.position.y
        var finH = otos.position.h

        Singleton.finalXInches = finX
        Singleton.finalYInches = finY
        Singleton.finalHeadingDeg = finH
        Singleton.team = "Blue"
        Singleton.starting = "Close"
        Singleton.tagTracking = 0
        Singleton.autoRan = true
        Turret.moveToTick(0)

    }
}