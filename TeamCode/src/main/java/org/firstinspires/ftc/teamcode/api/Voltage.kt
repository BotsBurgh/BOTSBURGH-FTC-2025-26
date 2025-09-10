package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.core.logging.Logging

/**
 * An API that enables access to the voltage of the control hub.
 *
 * @see get
 */
object Voltage : API() {
    private lateinit var sensor: VoltageSensor
    private val log = Logging.logger(this)

    override fun init(opMode: OpMode) {
        super.init(opMode)

        val voltageSensors = this.opMode.hardwareMap.voltageSensor

        if (voltageSensors.size() > 1) {
            this.log.warn("More than one voltage sensor detected, using the first one.")
        }

        // Get the sensor from the list of sensors, since we don't know its name.
        this.sensor = voltageSensors.iterator().next()
    }

    /**
     * Returns the current voltage of the control hub.
     */
    fun get(): Double = this.sensor.voltage
}
