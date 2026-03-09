package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.sqrt

@Autonomous(name = "FarRed")
class autoFarRed6: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()
        RobotTracker.setPos(84.0, 8.75, 90.0, true)

        telemetry.addData("x", RobotTracker.getPos(true)[0])
        telemetry.addData("x", RobotTracker.getPos(true)[1])
        telemetry.addData("x", RobotTracker.getPos(true)[2])
        telemetry.addData("distance", sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(true)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(true)[1]).squared()))
        telemetry.update()

        waitForStart()
        Singleton.team = "Red"
        Singleton.starting = "Far"
        Singleton.tagTracking = 1
        Singleton.autoRan = true

        //Move to Lower 3 balls
        SpecterDrive.path(0.0, 30.0, 0.0, 2.0)
        telemetry.addData("X", RobotTracker.getPos(true)[0])
        telemetry.addData("X", RobotTracker.getPos(true)[1])
        telemetry.addData("X", RobotTracker.getPos(true)[2])
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


        //Singleton logging
        var finX = RobotTracker.getPos(true)[0]
        var finY = RobotTracker.getPos(true)[1]
        var finH = RobotTracker.getPos(true)[2]

        Singleton.finalXInches = finX
        Singleton.finalYInches = finY
        Singleton.finalHeadingDeg = finH

        Turret.moveToTick(0)

    }

}