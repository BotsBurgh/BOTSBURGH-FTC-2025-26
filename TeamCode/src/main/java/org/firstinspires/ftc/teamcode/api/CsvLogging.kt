package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * An API for creating, managing, and writing to logging files.
 */
object CsvLogging : API() {

    //queue of all pending log lines per file.
    private val buffer = mutableMapOf<String, MutableList<String>>()

    //Actual writers created only on flush/close.
    private val writers = mutableMapOf<String, BufferedWriter>()

    override fun init(opMode: OpMode) {
        super.init(opMode)
        buffer.clear()
        writers.clear()
    }

    /**
     * Creates a new CSV file in the buffer. Does NOT touch storage yet.
     * */
    fun createFile(fileName: String) {
        buffer[fileName] = mutableListOf()
    }

    /**
     * Write a double entry to RAM buffer.
     * */
    fun writeFile(file: String, data: Double) {
        val list = buffer[file] ?: return
        list.add("${opMode.runtime}, $data")
    }

    /**
     * Write an array entry to RAM buffer.
     * */
    fun writeFile(file: String, data: Array<Double>) {
        val list = buffer[file] ?: return
        val builder = StringBuilder()
        builder.append(opMode.runtime)
        for (d in data) builder.append(", ").append(d)
        list.add(builder.toString())
    }

    /**
     * Flushes the RAM buffer to storage safely.
     * Will create writer lazily (if they cant do it, then they will abort instead of shutting down) and handle errors.
     */
    fun flush(file: String) {
        val list = buffer[file] ?: return
        if (list.isEmpty()) return

        val writer = writers.getOrPut(file) {
            createWriter(file) ?: return   // If file cannot be created, skip
        }

        try {
            for (line in list) {
                writer.write(line)
                writer.newLine()
            }
            writer.flush()
            list.clear()   // only clear after successful flush
        } catch (_: IOException) {
            // DO NOT CRASH THE ROBOT CONTROLLER
            // If writing fails, just stop.
        }
    }

    /**
     * Close all writers safely.
     */
    fun close() {
        for (writer in writers.values) {
            try {
                writer.flush()
                writer.close()
            } catch (_: IOException) {}
        }
        writers.clear()
    }

    /**
     * Creates a writer using FTC-safe storage.
     */
    private fun createWriter(name: String): BufferedWriter? {
        return try {
            val file: File = AppUtil.getInstance().getSettingsFile("$name.csv")
            BufferedWriter(FileWriter(file, true))
        } catch (_: Exception) {
            null   // If file creation fails, do not crash
        }
    }
}