package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.api.TriWheels

@Autonomous(name = "autoMain")

class autoMain : LinearOpMode(){

    override fun runOpMode(){
        TriWheels.init(this)
        SpecterDrive.init(this)
        RobotTracker.autoInit(this)

        waitForStart()
    }

}