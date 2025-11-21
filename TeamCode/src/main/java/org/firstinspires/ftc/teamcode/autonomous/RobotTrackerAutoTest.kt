package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "RobotTrackerAutoTest", group = "Test")

class RobotTrackerAutoTest: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        RobotTracker.init(this)

        waitForStart()

        SpecterDrive.linearPath(-2.00, 2.19, 0.0)
        RobotTracker.addPos(-2.00, 2.19, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(-0.07, 1.98, 0.0)
        RobotTracker.addPos(-0.07, 1.98, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(2.06, -4.23, 0.0)
        RobotTracker.addPos(2.06, -4.23, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(-6.02, 1.84, 0.0)
        RobotTracker.addPos(-6.02, 1.84, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(-0.07, 2.26, 0.0)
        RobotTracker.addPos(-0.07, 2.26, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(0.12, -2.31, 0.0)
        RobotTracker.addPos(0.12, -2.31, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()
        sleep(100)

        SpecterDrive.linearPath(5.96, -1.84, 0.0)
        RobotTracker.addPos(5.96, -1.84, 0.0)
        telemetry.addData("Cpos:", RobotTracker.getPos())
        telemetry.update()

        sleep(100)

    }


}