package org.firstinspires.ftc.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotConfig
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

        RobotTracker.setPos(84.0 , 8.75, 90.0, true)
        waitForStart()
        Turret.changeTargetVelocity(sqrt((RobotConfig.UniversalCoordinates.RED_POS[0]- RobotTracker.getPos(true)[0]).squared()+(RobotConfig.UniversalCoordinates.RED_POS[1]- RobotTracker.getPos(true)[1]).squared()))
        Turret.moveToTick(-383)
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)
        sleep(2500)
        Turret.launch(-0.01)

        TransferSystem.setIntakePwr(-1.0)
        SpecterDrive.path(-5.0, 0.0, 0.0)
        SpecterDrive.path(0.0, 49.0, 0.0)
        sleep(2000)
        SpecterDrive.path(0.0, -50.0, 0.0)


        TransferSystem.setTransferPwr(0.0)
        TransferSystem.setIntakePwr(0.0)
        Turret.launch()
        sleep(2000)
        TransferSystem.setTransferPwr(-1.0)
        TransferSystem.setIntakePwr(1.0)

    }
}