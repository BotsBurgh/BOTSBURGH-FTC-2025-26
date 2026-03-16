package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.Voltage
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive.otos



@Autonomous(name = "CLOSE RED")
class autoCloseRed9: LinearOpMode() {


    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Voltage.init(this)
        Singleton.reset()

        RobotTracker.setPos(125.5, 118.5, 309.13, true)

        waitForStart()

        //Move back and charge turret
        Turret.changeTargetVelocity(90.0, true)
        Turret.launch()
        TransferSystem.setIntakePwr(-1.0)
        SpecterDrive.path(0.0, -50.0, 0.0, 3.0)

        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2000)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch(0.5)

        //Rotate to 90
        SpecterDrive.rotateToHeading(280.0, 0.7)

        //Forward and intake first 3 balls
        Turret.moveToTick(-240)
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (4.5*12.0), 0.0, 2.5)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        Turret.launch()
        SpecterDrive.path(0.0, -(4.5*12.0), 0.0, 1.75)
        Turret.changeTargetVelocity(80.0, true)




        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch(0.5)

        //Down
        SpecterDrive.path(23.5, 0.0, 0.0, 1.5)

        //Forward and intake next 3 balls, hit classifier
        Turret.launch()
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (5.5*12.0), 0.0, 3.0)
        TransferSystem.intake()
        TransferSystem.reverseTransfer()
        SpecterDrive.path(0.0, -12.0, 0.0, 2.0)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        SpecterDrive.path(-12.0, 0.0, 0.0, 2.0)
        SpecterDrive.path(0.0, 18.0, 0.0, 1.0)
        Turret.launch(0.5)
        SpecterDrive.path(0.0, -(5.5*12.0)-6.7, 0.0, 1.5)
        Turret.changeTargetVelocity(66.9, true)

        Turret.launch()

        //Up
        SpecterDrive.path(-30.0, 0.0, 0.0, 1.5)

        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        //Up
        SpecterDrive.path(-15.0, 0.0, 0.0, 1.5)


        //Singleton logging
        var finX = otos.position.x
        var finY = otos.position.y
        var finH = otos.position.h

        Singleton.finalXInches = finX
        Singleton.finalYInches = finY
        Singleton.finalHeadingDeg = finH
        Singleton.team = "Red"
        Singleton.starting = "Close"
        Singleton.tagTracking = 1
        Singleton.autoRan = true
        Turret.moveToTick(0)

    }
}