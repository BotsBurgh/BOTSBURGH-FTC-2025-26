package org.firstinspires.ftc.teamcode.core.logging

import org.threeten.bp.Instant

/**
 * A simple [Logger] that prints to the standard output.
 *
 * In most cases you should prefer [FTCLogger] unless it is unavailable, such as when running unit
 * tests.
 */
class SimpleLogger(tag: String) : Logger(tag) {
    override fun log(
        level: Level,
        msg: String,
    ) {
        println("[${Instant.now()} ${level.name} ${this.tag}] $msg")
    }
}
