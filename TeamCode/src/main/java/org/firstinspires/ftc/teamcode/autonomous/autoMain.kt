package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

@Autonomous(name = "AutoMain", group = "Main")
class AutoMain : LinearOpMode() {

    override fun runOpMode() {
        telemetry.addLine("Initializing...")
        telemetry.update()

        var tagID = -1
        telemetry.addLine("Scanning for AprilTag...")
        telemetry.update()

        while (!isStarted && !isStopRequested) {
            tagID = getTagID()
            telemetry.addData("Detected Tag ID", tagID)
            telemetry.update()
            sleep(100)
        }

        waitForStart()

        telemetry.addData("Chosen Autonomous", tagID)
        telemetry.update()

        when (tagID) {
            1 -> runAutoLeft()
            2 -> runAutoMiddle()
            3 -> runAutoRight()
        }

        telemetry.addLine("Autonomous Complete")
        telemetry.update()
    }

    private fun getTagID(): Int {
        return try {
            val url = URL("http://limelight.local:5800/json")
            val input = BufferedReader(InputStreamReader(url.openStream()))
            val json = input.readLine()
            val data = JSONObject(json)

            val results: JSONArray = data.getJSONArray("Results")
            if (results.length() > 0) {
                val tag = results.getJSONObject(0)
                tag.getInt("fiducial_id")
            } else {
                -1
            }
        } catch (e: Exception) {
            telemetry.addData("Limelight Error", e.toString())
            -1
        }
    }

    private fun runAutoLeft() {
        telemetry.addLine("Running LEFT Auto (Tag 1)")
        telemetry.update()
    }

    private fun runAutoMiddle() {
        telemetry.addLine("Running MIDDLE Auto (Tag 2)")
        telemetry.update()
    }

    private fun runAutoRight() {
        telemetry.addLine("Running RIGHT Auto (Tag 3)")
        telemetry.update()
    }
}
