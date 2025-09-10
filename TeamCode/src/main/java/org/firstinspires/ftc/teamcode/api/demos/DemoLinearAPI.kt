package org.firstinspires.ftc.teamcode.api.demos

import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.api.TriWheels
import org.firstinspires.ftc.teamcode.core.API
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * This is a demo API for driving related to april tags.
 *
 * Requires the [TriWheels] API.
 */
object DemoLinearAPI : API() {
    // `isLinear` requires that this API is only initialized with a `LinearOpMode`.
    // By enabling it, you can access `this.linearOpMode`.
    override val isLinear = true

    // This API uses the `TriWheels` API. By setting it as a dependency, the opmode will crash if
    // it is not also initialized.
    override val dependencies = setOf(TriWheels)

    // The max speed for individual components of the movement
    private const val MAX_RANGE = 0.3
    private const val MAX_HEADING = 0.5
    private const val MAX_STRAFE = 0.4

    /**
     * Drives in front of the given april tag [tagId], facing squarely in front of it
     * [desiredDistance] inches away.
     */
    fun driveTo(
        tagId: Int,
        desiredDistance: Double = 12.0,
    ) = with(linearOpMode) {
        // Initialize errors with a really large number so while loop runs at least once
        // These are placeholders that get overridden in the loop
        var rangeError = Double.MAX_VALUE
        var headingError = Double.MAX_VALUE
        var strafeError = Double.MAX_VALUE

        // Drive while opmode is active and target has not been reached
        while (
            opModeIsActive()
        ) {
            // Try scanning for the april tag

            // If no tag of the expected ID is found
            // Stop moving, hoping that the tag will be scanned
            TriWheels.stop()

            with(telemetry) {
                addData("Status", "Tag not found")
                update()
            }

            sleep(100)

            continue
        }

        // Calculate how far we are from the target position
        rangeError = 1.0
        headingError = 2.0
        strafeError = 3.0

        // Calculate the wheel power from the error
        val range =
            Range.clip(
                rangeError,
                -MAX_RANGE,
                MAX_RANGE,
            )
        val heading =
            Range.clip(
                headingError,
                -MAX_HEADING,
                MAX_HEADING,
            )
        val strafe =
            Range.clip(
                strafeError,
                -MAX_STRAFE,
                MAX_STRAFE,
            )

        // Treat range and strafe as an XY vector
        // Then convert to the polar coordinate system (angle, length)
        // This is the same math used to
        val radians = atan2(range, strafe)
        val magnitude = sqrt(range * range + strafe * strafe)

        // Set the power of the wheels
        TriWheels.drive(radians, magnitude, rotation = heading)

        // Stop the wheels from moving, the target has been reached!
        TriWheels.stop()
    }
}
