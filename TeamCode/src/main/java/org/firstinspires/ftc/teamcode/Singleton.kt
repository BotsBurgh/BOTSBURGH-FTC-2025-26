package org.firstinspires.ftc.teamcode

object Singleton {

    /**
     * Autonomous Check
     */
    var autoRan: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    /**
     * FTC Positions
     */
    var team: String = ""
        get() = field
        set(value) {
            field = value
        }

    var starting: String = ""
        get() = field
        set(value) {
            field = value
        }

    /**
     * Position Localization
     */
    var finalHeadingDeg: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var finalXInches: Double = 12.0
        get() = field
        set(value) {
            field = value
        }

    var finalYInches: Double = 12.0
        get() = field
        set(value) {
            field = value
        }

    var initXInches: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var initYInches: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var initHeadingDeg: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    /**
     * Limelight
     */
    var tagTracking: Int = -1
        get() = field
        set(value) {
            field = value
        }

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