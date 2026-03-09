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
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive.otos
import org.firstinspires.ftc.teamcode.utils.squared
import kotlin.math.sqrt

@Autonomous(name = "test")
class autoTest: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Singleton.reset()
        RobotTracker.setPos(0.0, 0.0, 270.0, true)

        waitForStart()

        SpecterDrive.path(0.0, 30.0, otos.position.h)
        //SpecterDrive.path(30.0, 0.0, 0.0)

    }

}