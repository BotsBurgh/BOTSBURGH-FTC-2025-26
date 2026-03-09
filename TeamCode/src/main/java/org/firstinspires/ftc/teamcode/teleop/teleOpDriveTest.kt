import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.RobotConfig
import org.firstinspires.ftc.teamcode.api.RobotTracker
import org.firstinspires.ftc.teamcode.api.TriWheels
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

@TeleOp(name = "teleOpDriveTest")
@Disabled
class teleOpDriveTest : OpMode() {

    override fun init() {
        TriWheels.init(this)
        RobotTracker.teleInit(this)
    }

    override fun loop() {
        //updates first
        RobotTracker.updatePos()
        telemetry.clear()

        // joystick(Movement) input
        val joyX = -this.gamepad1.left_stick_x.toDouble()
        val joyY = this.gamepad1.left_stick_y.toDouble()

        // PI / 3 because 0 radians is right, not forward
        val joyRadians = atan2(joyY, joyX) - (PI / 3.0) - (2.0 * PI / 3.0)

        val joyMagnitude = sqrt(joyY * joyY + joyX * joyX)

        val rotationPower = 0 - this.gamepad1.right_stick_x.toDouble()

        // movement of all wheels
        TriWheels.driveWithOffset(
            joyRadians,
            joyMagnitude * RobotConfig.TeleOpMain.DRIVE_SPEED,
            rotation = rotationPower * RobotConfig.TeleOpMain.ROTATE_SPEED,
        )
    }
}