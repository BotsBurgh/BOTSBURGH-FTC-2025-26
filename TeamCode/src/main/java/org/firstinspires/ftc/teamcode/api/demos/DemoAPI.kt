package org.firstinspires.ftc.teamcode.api.demos

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.core.API

/**
 * This is a demo API used for controlling a robot with four mecanum wheels.
 */
object DemoAPI : API() {
    // The wheel motors
    // `lateinit` means the variable does not have to be initialized in the constructor
    // See: https://github.com/BotsBurgh/BOTSBURGH-FTC-2023-24/pull/7#discussion_r1342154226
    private lateinit var fl: DcMotor
    private lateinit var fr: DcMotor
    private lateinit var bl: DcMotor
    private lateinit var br: DcMotor

    // `init()` is called by the opmode in order to use this API.
    // Override it if you need do things at startup, such as accessing hardware.
    override fun init(opMode: OpMode) {
        // You must call super.init(opMode), or the API will not initialize correctly.
        super.init(opMode)

        fl = this.opMode.hardwareMap.get(DcMotor::class.java, "fl")
        fr = this.opMode.hardwareMap.get(DcMotor::class.java, "fr")
        bl = this.opMode.hardwareMap.get(DcMotor::class.java, "bl")
        br = this.opMode.hardwareMap.get(DcMotor::class.java, "br")
    }

    // A public function that sets the power of all four wheels.
    // This is callable by any components.
    fun drive(
        fl: Double,
        fr: Double,
        bl: Double,
        br: Double,
    ) {
        this.fl.power = fl
        this.fr.power = fr
        this.bl.power = bl
        this.br.power = br
    }

    fun drive(power: Double) {
        this.drive(power, power, power, power)
    }
}
