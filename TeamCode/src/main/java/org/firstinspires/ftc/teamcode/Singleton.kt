package org.firstinspires.ftc.teamcode

/**
 * A Singleton is  pattern that restricts a class to having only one instance (object)
 * throughout the entire application's lifecycle, while providing a global point of access to that instance.
 *
 * If theres data in autonomous that needs to be transferred to teleop, make it a variable here
 */
object Singleton {
    /**
     * Autonomous Check
     */
    var autoRan: Boolean = false

    /**
     * FTC Positions
     */
    var team: String = ""

    var starting: String = ""

    /**
     * Position Localization
     */
    var finalHeadingDeg: Double = 0.0
    var finalXInches: Double = 0.0
    var finalYInches: Double = 0.0

    var initXInches: Double = 0.0

    var initYInches: Double = 0.0

    var initHeadingDeg: Double = 0.0

    /**
     * Limelight
     */
    var tagTracking: Int = -1

    /**
     * Reset all to base
     */
    fun reset() {
        autoRan = false
        finalXInches = 0.0
        finalYInches = 0.0
        finalHeadingDeg = 0.0
        initXInches = 0.0
        initYInches = 0.0
        initHeadingDeg = 0.0
        team = ""
        starting = ""
        tagTracking = -1
    }
}
