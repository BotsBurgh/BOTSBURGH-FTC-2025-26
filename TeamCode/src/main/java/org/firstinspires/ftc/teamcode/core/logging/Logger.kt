package org.firstinspires.ftc.teamcode.core.logging

abstract class Logger(protected val tag: String) {
    /**
     * Logs a message with a specific level.
     *
     * There are various shorthand functions for this, such as [info] and [warn].
     */
    abstract fun log(
        level: Level,
        msg: String,
    )

    /**
     * Logs a debugging message.
     */
    fun debug(msg: Any) = this.log(Level.Debug, msg.toString())

    /**
     * Logs an info message.
     */
    fun info(msg: Any) = this.log(Level.Info, msg.toString())

    /**
     * Logs a warning message.
     */
    fun warn(msg: Any) = this.log(Level.Warn, msg.toString())

    /**
     * Logs an error message.
     */
    fun error(msg: Any) = this.log(Level.Error, msg.toString())
}
