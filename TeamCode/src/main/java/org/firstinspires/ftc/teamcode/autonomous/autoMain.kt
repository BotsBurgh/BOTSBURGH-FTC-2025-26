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
        RobotTracker.init(this)
        CsvLogging.init(this)

        waitForStart()

        //SpecterDrive.linearPath(x,y,h)
        //RobotTracker.addPos(x,y,h)
        //do something


        ///...end of auto
        RobotTracker.logPos(0.0, 0.0)
    }
}