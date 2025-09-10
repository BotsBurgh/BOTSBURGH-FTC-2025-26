package org.firstinspires.ftc.teamcode.core.logging

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.RobotConfig
import org.threeten.bp.Instant
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.zip.GZIPOutputStream

/**
 * A general-purpose [Logger] used when running on the robot.
 *
 * This emits log messages to the Android Logger (see [android.util.Log]), the `BotsBurgh` folder on
 * the file system, and the driver station telemetry. The driver station telemetry is filtered based
 * on the log level.
 *
 * @see RobotConfig.Logging
 * @see SimpleLogger
 */
class FTCLogger(tag: String) : Logger(tag) {
    override fun log(
        level: Level,
        msg: String,
    ) {
        val formattedMessage = "[${Instant.now()} ${level.name} ${this.tag}] $msg"

        // Log message to Android's logger, accessible through Android Studio.
        Log.println(level.priority, this.tag, msg)

        // Write formatted message to the compressed log file.
        compressedStream.write((formattedMessage + '\n').toByteArray())

        // Log the message to the driver station if its level is not filtered out. (For example,
        // debug (priority of 3) messages are skipped when the filter is set to warn (priority
        // of 5), but warn and error (priority of 6) messages are still displayed.
        if (RobotConfig.Logging.FILTER_LEVEL.priority <= level.priority) {
            telemetryLog.add(formattedMessage)
        }
    }

    companion object {
        private lateinit var opMode: OpMode
        private lateinit var compressedFile: File
        private lateinit var compressedStream: OutputStream
        private lateinit var telemetryLog: Telemetry.Log

        fun init(opMode: OpMode) {
            this.opMode = opMode

            // Create the log folder, if it does not exist already.
            RobotConfig.Logging.LOG_FOLDER.mkdirs()

            // Create compressed file.
            this.compressedFile = File(RobotConfig.Logging.LOG_FOLDER, "${Instant.now()}.txt.gz")
            this.compressedFile.createNewFile()

            // Create buffered stream for compressed file.
            this.compressedStream = GZIPOutputStream(FileOutputStream(this.compressedFile)).buffered()

            // Configure telemetry log.
            this.telemetryLog = this.opMode.telemetry.log()
            this.telemetryLog.capacity = RobotConfig.Logging.TELEMETRY_CAPACITY
            this.telemetryLog.displayOrder = RobotConfig.Logging.TELEMETRY_ORDER
        }

        fun flush() {
            this.compressedStream.flush()
            this.opMode.telemetry.update()
        }

        fun close() {
            this.compressedStream.close()
        }
    }
}
