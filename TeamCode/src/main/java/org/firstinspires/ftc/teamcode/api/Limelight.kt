package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult

object Limelight {
    private lateinit var cam: Limelight3A
    var latestTx = 0.0
        private set
    private var targetVisible = false

    fun init(opMode: OpMode) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")
        cam.pipelineSwitch(0)   // pipeline 0 should be AprilTags
    }

    fun update() {
        val result = cam.latestResult
        if (result != null && result.isValid) {
            val fiducials: List<FiducialResult> = result.fiducialResults

            val tag21 = fiducials.firstOrNull { it.fiducialId == 21 }

            if (tag21 != null) {
                targetVisible = true
                latestTx = tag21.targetXDegrees   // horizontal offset in degrees

                val kP = 0.015
                Turret.aimer.mode = DcMotor.RunMode.RUN_USING_ENCODER
                Turret.aimer.power = latestTx * kP
            } else {
                targetVisible = false
                latestTx = 0.0
                Turret.stop()
            }
        } else {
            targetVisible = false
            latestTx = 0.0
            Turret.stop()
        }
    }

    fun seesTag21(): Boolean {
        return targetVisible
    }
}
