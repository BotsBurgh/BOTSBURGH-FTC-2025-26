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
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.sqrt

@Autonomous(name = "CLOSE RED")
class autoCloseRed9: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(60.0, 8.0, 90.0, true) //find real pos
        //Start Position: Angled against the wall, midpoint
        waitForStart()

        //Move back and charge turret
        Turret.changeTargetVelocity(
            sqrt(
                (RobotConfig.UniversalCoordinates.RED_POS[0] - (6.69 * 12)).squared() +
                        (RobotConfig.UniversalCoordinates.RED_POS[1] - (7.11 * 12)).squared()
            )  +5.5
        )
        Turret.launch()
        SpecterDrive.path(0.0, -50.0, 0.0, 3.0)

        //Fire and shut down
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(1000)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch(0.5)

        //Rotate to 90
        SpecterDrive.rotateToHeading(-30.0, 0.7)

        //Forward and intake first 3 balls
        Turret.moveToTick(-230) //FIND TICK
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (4.5*12.0), 0.0, 3.0)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        Turret.launch()
        SpecterDrive.path(0.0, -(4.5*12.0), 0.0, 1.75)

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
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, (5.5*12.0), 0.0, 3.0)
        SpecterDrive.path(0.0, -12.0, 0.0, 2.0)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)
        SpecterDrive.path(-12.0, 0.0, 0.0, 2.0)
        SpecterDrive.path(0.0, 18.0, 0.0, 1.0)
        Turret.launch(-0.5)
        SpecterDrive.path(0.0, -(5.5*12.0)-6.7, 0.0, 1.5)

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
        Singleton.autoRan = true
        Singleton.finalXInches = RobotTracker.getPos(true)[0]
        Singleton.finalYInches = RobotTracker.getPos(true)[1]
        Singleton.finalHeadingDeg = RobotTracker.getPos(true)[2]
        Singleton.team = "Red"
        Singleton.starting = "Close"
        Singleton.tagTracking = 1


    }
}