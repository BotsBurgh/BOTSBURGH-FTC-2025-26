package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.CsvLogging
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.api.TriWheels
import com.qualcomm.robotcore.hardware.DcMotor


@Autonomous(name = "autoMain")

class autoMain : LinearOpMode(){

    override fun runOpMode(){
        TriWheels.init(this)
        SpecterDrive.init(this)
        RobotTracker.autoInit(this)

        waitForStart()


        if (opModeIsActive()) {
            TriWheels.green.mode = DcMotor.RunMode.RUN_USING_ENCODER
            TriWheels.blue.mode = DcMotor.RunMode.RUN_USING_ENCODER
            TriWheels.blue.power = -1.0     // small gentle push forward
            TriWheels.green.power = 1.0    // small gentle push forward

            sleep(5000)


            TriWheels.blue.power = 0.0     // stop
            TriWheels.green.power = 0.0     // stop

            sleep(500)


        }




    }

}