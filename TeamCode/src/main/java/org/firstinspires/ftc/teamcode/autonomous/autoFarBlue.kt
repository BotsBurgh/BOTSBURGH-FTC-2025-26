package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "autoFarBlue(9ball)")
class autoFarBlue: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        TransferSystem.init(this)
        Turret.init(this)

        waitForStart()

        //Fire preloads

        //Rotate towards balls
        SpecterDrive.rotate(270.0, 0.5)
        sleep(100)

        //Intake balls


        //Move to balls
        SpecterDrive.linearPath(52.72, -0.76, 0.0)
        sleep(100)

        //Stop Intake

        //Move back to start
        SpecterDrive.linearPath(-52.42, 0.45, 0.0)
        sleep(100)

        //Fire balls

        //Move to balls x pos
        SpecterDrive.linearPath(21.60, 23.26, 0.0)
        sleep(100)

        //Intake on

        //Intake balls
        SpecterDrive.linearPath(24.02, 0.15, 0.0)
        sleep(100)

        //Stop intake

        //Move back to start
        SpecterDrive.linearPath(-45.32, -23.56, 0.0)
        sleep(100)

        //Fire

        //leave
        SpecterDrive.linearPath(0.0, 24.0, 0.0)
        sleep(100)

    }
}