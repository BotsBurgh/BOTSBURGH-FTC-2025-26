package org.firstinspires.ftc.teamcode.api

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.core.API

object Limelight : API() {
    private lateinit var cam: Limelight3A
    private var targetId: Int = -1

    var angleX = 0.0
        private set
    var seesTag = false
        private set

    override fun init(opMode: OpMode) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")
        cam.pipelineSwitch(0)
        cam.start()
    }

    fun update() {
        val result = cam.latestResult

        if (result == null || !result.isValid) {
            seesTag = false
            angleX = 0.0
            return
        }

        val tag = if (targetId == -1) {
            result.fiducialResults.firstOrNull()
        } else {
            result.fiducialResults.find { it.fiducialId == targetId }
        }

        if (tag != null) {
            seesTag = true
            angleX = tag.targetXDegrees
        } else {
            seesTag = false
            angleX = 0.0
        }
    }

    fun changeTagID(id: Int) {
        targetId = id
        cam.pipelineSwitch(if (id == 24) 1
        else 0)
    }
}