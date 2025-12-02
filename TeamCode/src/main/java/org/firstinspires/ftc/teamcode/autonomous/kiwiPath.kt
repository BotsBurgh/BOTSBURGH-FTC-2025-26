package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "KiwiPath")
class kiwiPath: LinearOpMode() {
    override fun runOpMode() {

        SpecterDrive.init(this)
        TriWheels.init(this)
        CsvLogging.init(this)
        //RobotTracker.autoInit(this)

        waitForStart()
        SpecterDrive.linearPath(-2.03, 2.18, 0.0)
        //RobotTracker.addPos(-2.03, 2.18, 0.0, true)
        sleep(100)

        SpecterDrive.linearPath(-0.06, 1.62, 0.0)
        //RobotTracker.addPos(-0.06, 1.62, 0.0, true)
        sleep(100)

        SpecterDrive.linearPath(2.11, -3.81, 0.0)
        //RobotTracker.addPos(2.11, -3.81, 0.0, true)
        sleep(100)

        SpecterDrive.linearPath(-6.22, 0.05, 0.0)
        //RobotTracker.addPos(-6.22, 0.05, 0.0, true)
        sleep(100)

        //RobotTracker.logPos(1.0, 1.0)
    }
}