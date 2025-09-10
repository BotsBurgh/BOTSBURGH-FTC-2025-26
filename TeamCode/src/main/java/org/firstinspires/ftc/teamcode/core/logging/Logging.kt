package org.firstinspires.ftc.teamcode.core.logging

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.core.CoreRegistrant

/**
 * An API for logging information to the telemetry, a compressed log file, and Android's standard
 * logger.
 *
 * This API is automatically initialized by [CoreRegistrant].
 *
 * This API purposefully does not implement [API], since [API] depends on this being initialized.
 */
object Logging {
    private val log = this.logger(this)

    private val androidLogAvailable =
        try {
            Log.d("Logging", "Detecting if the Android logger is available.")
            true
        } catch (_: RuntimeException) {
            false
        }

    fun init(opMode: OpMode) {
        if (this.androidLogAvailable) {
            FTCLogger.init(opMode)
        }

        this.log.debug("Logging initialized.")
    }

    fun logger(tag: String): Logger =
        if (this.androidLogAvailable) {
            FTCLogger(tag)
        } else {
            SimpleLogger(tag)
        }

    /**
     * Creates a new [Logger] where the tag is the name of the class.
     */
    fun logger(clazz: Any): Logger = logger(clazz::class.simpleName ?: "Unknown")
}
