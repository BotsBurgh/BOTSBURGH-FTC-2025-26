import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.api.TransferSystem
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.api.Turret
import org.firstinspires.ftc.teamcode.api.linear.SpecterDrive
import org.firstinspires.ftc.teamcode.Singleton
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.RobotTracker

@Autonomous(name = "Test")
class NewAutoTest: LinearOpMode() {
    var max = 999.99999
    override fun runOpMode() {
        SpecterDrive.init(this)
        TriWheels.init(this)
        Turret.init(this)
        RobotTracker.autoInit(this)

        SpecterDrive.otos.position = RobotConfig.UniversalCoordinates.CLOSE_RED_POS

        waitForStart()

        //Robot-Centric Testing
        SpecterDrive.path(0.0, -24.0, 0.0)
        sleep(10)
        SpecterDrive.path(-24.0, -24.0, 0.0)
        sleep(10)

        //Turret Testing 1
        Turret.trackPos(true)

        //Field-Centric Testing
        SpecterDrive.path(RobotConfig.UniversalCoordinates.red_line_close, 0.0, max, false)
        sleep(10)
        SpecterDrive.path(RobotConfig.UniversalCoordinates.red_line_mid, 0.0, max, false)
        sleep(10)
        SpecterDrive.path(RobotConfig.UniversalCoordinates.red_line_far, 0.0, max, false)

        //Turret Testing 2
        Turret.trackPos(true)

        //Singleton testing

        Singleton.autoRan = true
        Singleton.finalXInches = RobotTracker.getPos(true)[0]
        Singleton.finalXInches = RobotTracker.getPos(true)[1]
        Singleton.finalXInches = RobotTracker.getPos(true)[2]
        Singleton.team = "Red"
        Singleton.starting = "Close"

    }
}