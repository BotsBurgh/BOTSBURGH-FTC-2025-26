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
import kotlin.concurrent.timer
import kotlin.math.sqrt

@Autonomous(name = "autoFarBlue(9 ball)")
class autoFarBlue6: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(60.0, 8.0, -90.0, true)
        waitForStart()

        //Launch 3 balls
        Turret.changeTargetVelocity(142.5)
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
        SpecterDrive.path(0.0, 42.0, 0.0, 2.5)
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        SpecterDrive.path(0.0, -42.0, 0.0, 2.5)
        SpecterDrive.path(-25.0, -0.0, 0.0, 2.0)

        //FIRE
        Turret.moveToTick(387)
        Turret.launch()
        sleep(1900)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2350)
        Turret.stop()
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Move to human Player
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(-1.0)
        Turret.launch(0.1)

        SpecterDrive.path(0.0, 72.0, 0.0, 2.5, true, 15.0)
        sleep(3500)
        SpecterDrive.path(0.0, -55.0, 0.0, 2.0, true, 10.0)
        TransferSystem.setIntakePwr(-1.0)
        TransferSystem.setTransferPwr(0.0)


        //FIRE
        Turret.moveToTick(387)
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2350)
        Turret.stop()
        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)

        //Escape
        SpecterDrive.path(0.0, 40.0, 0.0)

        //Singleton logging
        Singleton.autoRan = true
        Singleton.finalXInches = RobotTracker.getPos(true)[0]
        Singleton.finalXInches = RobotTracker.getPos(true)[1]
        Singleton.finalXInches = RobotTracker.getPos(true)[2]
        Singleton.team = "Blue"
        Singleton.starting = "Far"
        Singleton.tagTracking = 0
    }
}