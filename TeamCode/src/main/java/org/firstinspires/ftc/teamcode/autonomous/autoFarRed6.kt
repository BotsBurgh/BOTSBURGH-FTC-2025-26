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

@Autonomous(name = "autoFarRed(9 ball)")
class autoFarRed6: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(84.0, 8.75, 90.0, true)
        SpecterDrive.log((sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(true)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(true)[1]).squared())).toString())
        waitForStart()

        //Launch 3 balls
        Turret.changeTargetVelocity(164.5460567)

        Turret.moveToTick(-390)
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        Turret.launch(0.5)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Move to Lower 3 balls
        SpecterDrive.path(-30.0, 0.0, 0.0, 2.0)
        Turret.stop()
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 48.0, 0.0, 2.5)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        SpecterDrive.path(0.0, -48.0, 0.0, 2.5)
        SpecterDrive.path(30.0, -0.0, 0.0, 2.0)

        //FIRE
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        Turret.stop()
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Move to human Player
        Turret.launch(0.5)
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        SpecterDrive.path(0.0, 72.0, 0.0, 3.0)
        SpecterDrive.path(0.0, -12.0, 0.0, 1.0)
        SpecterDrive.path(0.0, 12.0, 0.0, 3.0)
        SpecterDrive.path(0.0, -72.0, 0.0, 2.0)
        TransferSystem.setIntakePwr(0.0)
        TransferSystem.setTransferPwr(0.0)


        //FIRE
        Turret.moveToTick(-400) //change
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        Turret.stop()
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Escape
        SpecterDrive.path(0.0, 24.0, 0.0)

        //Singleton logging
        Singleton.autoRan = true
        Singleton.finalXInches = RobotTracker.getPos(true)[0]
        Singleton.finalXInches = RobotTracker.getPos(true)[1]
        Singleton.finalXInches = RobotTracker.getPos(true)[2]
        Singleton.team = "Red"
        Singleton.starting = "Far"
        Singleton.tagTracking = 1
    }
}