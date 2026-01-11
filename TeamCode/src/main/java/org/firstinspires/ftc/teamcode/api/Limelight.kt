package org.firstinspires.ftc.teamcode.api
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.core.API

object Limelight: API(){
//cam is the variable for the limelight
    private lateinit var cam: Limelight3A
    //this is the target ID to change the pipeline to the ID for the limelight to give results on
    private var targetId: Int = -1
// the angle given by the limelight
    var angleX = 0.0
        private set
    //A check to make sure the april tag is visible
    var seesTag = false
        private set

    override fun init(opMode: OpMode) {
        cam = opMode.hardwareMap.get(Limelight3A::class.java, "limelight")

//the default limelight to target april tag
        cam.pipelineSwitch(0)
        cam.start()
    }

    fun update() {

        val result = cam.latestResult
//this is a measure to make sure it doesn't provide a value when nothiing is visible
        if (result == null || !result.isValid) {
            seesTag = false
            angleX = 0.0
            return
        }
//target ID
        val tag = if (targetId == -1) {
            result.fiducialResults.firstOrNull()
        } else {
            result.fiducialResults.find { it.fiducialId == targetId }
        }
//print the value
        if (tag != null) {
            seesTag = true
            angleX = tag.targetXDegrees
        } else {
            seesTag = false
            angleX = 0.0
        }
    }

//change the target ID
    fun changeTagID(id: Int) {
        targetId = id

        if (id == 24) {
            cam.pipelineSwitch(1)
        } else {
            cam.pipelineSwitch(0)
        }
    fun limelightInfo(){

    }
    }
}