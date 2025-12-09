package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult

/**
 * Reads AprilTag data from the Limelight3A camera.
 * Does NOT control any motors — Turret handles that.
 */
object Limelight {

    private lateinit var cam: Limelight3A

    // Which tag to aim at
    var targetTagId = 21

    // Horizontal angle offset to tag (degrees)
    var latestTx = 0.0
        private set

    // Whether the tag is visible
    private var targetVisible = false

    fun init(opMode: OpMode) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")
        cam.pipelineSwitch(0)  // AprilTag pipeline
    }

    fun update() {
        val result = cam.latestResult

        if (result != null && result.isValid) {

            val fiducials: List<FiducialResult> = result.fiducialResults
            val tag = fiducials.firstOrNull { it.fiducialId == targetTagId }

            if (tag != null) {
                targetVisible = true
                latestTx = tag.targetXDegrees
                return
            }
        }

        // No valid tag found
        targetVisible = false
        latestTx = 0.0
    }

    fun seesTag(): Boolean = targetVisible
}
