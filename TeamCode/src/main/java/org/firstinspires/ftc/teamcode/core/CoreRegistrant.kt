package org.firstinspires.ftc.teamcode.core

import android.content.Context
import com.qualcomm.ftccommon.FtcEventLoop
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier
import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop
import org.firstinspires.ftc.teamcode.core.logging.FTCLogger
import org.firstinspires.ftc.teamcode.core.logging.Logging

/**
 * This object is responsible for registering and running the core systems required for all opmodes.
 *
 * Its method [register] adds this object as a listener which then gets called when certain opmode
 * actions occur, as specified in [OpModeManagerNotifier]. So far, this object facilitates:
 *
 * - Resetting all [Resettable] variables at the start of an opmode.
 * - Setting up [Logging], and closing it after an opmode finishes.
 * - Checking [Dependencies] when an opmode is started.
 */
object CoreRegistrant : OpModeManagerNotifier.Notifications {
    override fun onOpModePreInit(opMode: OpMode) {
        // Reset all `Resettable` variables. This *must* happen before anything else, or old
        // values will leak into the program.
        Resettable.resetAll()

        // Initialize logging if a user-defined opmode is being run. (The `DefaultOpMode`, which is
        // run when no other opmode is running, is skipped.)
        if (opMode !is OpModeManagerImpl.DefaultOpMode) {
            Logging.init(opMode)
        }
    }

    override fun onOpModePreStart(opMode: OpMode) {
        // Check dependencies when running a user-defined opmode.
        if (opMode !is OpModeManagerImpl.DefaultOpMode) {
            Dependencies.checkDependencies()
        }
    }

    override fun onOpModePostStop(opMode: OpMode) {
        // Close and save the log file when running a user-defined opmode. This will crash if run
        // for `DefaultOpMode`, as `FTCLogger.close()` assumes a log file has already been created.
        if (opMode !is OpModeManagerImpl.DefaultOpMode) {
            FTCLogger.close()
        }
    }

    // This is an entrypoint that is called once at the beginning of the entire robot's lifecycle.
    @OnCreateEventLoop
    @JvmStatic
    fun register(
        @Suppress("UNUSED_PARAMETER")
        context: Context,
        ftcEventLoop: FtcEventLoop,
    ) {
        ftcEventLoop.opModeManager.registerListener(this)
    }
}
