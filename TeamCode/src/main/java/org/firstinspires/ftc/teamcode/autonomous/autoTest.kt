import com.qualcomm.robotcore.eventloop.opmode.Autonomous

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.PurePursuit
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive

@Autonomous(name = "TEST")
class autoTest: LinearOpMode() {
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        TransferSystem.init(this)
        Singleton.reset()

        RobotTracker.setPos(12.0, 12.0, 0.0, true)
        waitForStart()

        //local pos
        SpecterDrive.path(12.0, 0.0, 0.0)
        SpecterDrive.path(0.0, 12.0, 0.0)
        SpecterDrive.path(-12.0, 0.0, 0.0)
        SpecterDrive.path(0.0, -12.0, 0.0)

        //Diagonals
        SpecterDrive.path(12.0, 12.0, 0.0)

        //Turns
        SpecterDrive.path(12.0, 12.0, 90.0)
        SpecterDrive.path(-12.0, -12.0, 270.0)

        //Field pos
        SpecterDrive.path(RobotConfig.UniversalCoordinates.red_park, 0.0)
        SpecterDrive.path(RobotConfig.UniversalCoordinates.blue_line_far, 0.0)

        //Splines
        val spline=listOf(
            PurePursuit.PathPoint(48.0, 48.0),
            PurePursuit.PathPoint(60.0, 60.0),
            PurePursuit.PathPoint(96.0, 48.0),
            PurePursuit.PathPoint(120.0, 24.0)
        )

        SpecterDrive.followPurePursuit(PurePursuit(spline), 24.0)
    }
}
