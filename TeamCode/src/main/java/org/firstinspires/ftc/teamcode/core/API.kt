package org.firstinspires.ftc.teamcode.core

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.core.logging.Logging
import org.firstinspires.ftc.teamcode.utils.isNotNull

/**
 * An API is shared code used by [OpMode]s.
 *
 * APIs are used to abstract over common tasks, such as controlling the wheels, that can be shared
 * by multiple [OpMode]s. The must be initialized by the [OpMode] that uses them.
 */
abstract class API {
    private var nullableOpMode: OpMode? by Resettable { null }

    /**
     * Accesses the current [OpMode].
     *
     * This is a `protected` value, so only the API itself will be able to access this.
     *
     * @throws APINotInitialized If [init] is not called first.
     */
    protected val opMode: OpMode
        get() = this.nullableOpMode ?: throw APINotInitialized(this::class)

    /**
     * Accesses the current [OpMode] as a [LinearOpMode].
     *
     * @throws IllegalLinearOpModeAccess If [isLinear] is false.
     * @throws APINotInitialized If [init] is not called first.
     */
    protected val linearOpMode: LinearOpMode
        get() {
            if (!this.isLinear) {
                throw IllegalLinearOpModeAccess(this::class)
            }

            return this.opMode as LinearOpMode
        }

    /**
     * Returns true if [init] has been called for this opmode.
     *
     * Use this to detect if API can be used, or if it needs to be initialized.
     */
    val isInit: Boolean
        get() = this.nullableOpMode.isNotNull()

    /**
     * Defines whether this API requires features from [LinearOpMode].
     *
     * If set to true, [init] can only be called from within a [LinearOpMode]. This is useful if you
     * require methods such as [LinearOpMode.opModeInInit] or [LinearOpMode.sleep].
     *
     * @see linearOpMode
     */
    open val isLinear: Boolean = false

    /**
     * Configures all APIs that must be initialized in order to use this API.
     *
     * If dependencies are not initialized, [MissingDependency] will be thrown when the start button
     * is pressed.
     */
    open val dependencies: Set<API> = emptySet()

    /**
     * Initializes this API.
     *
     * # In [OpMode] and [LinearOpMode]
     *
     * Call [init] during the initialization stage.
     *
     * # In [API]
     *
     * Override this function to run custom initialization, but make sure to call
     * `super.init(opMode)` or the program will crash.
     */
    open fun init(opMode: OpMode) {
        // You can only initialize an API once without tearing it down.
        if (this.nullableOpMode.isNotNull()) {
            throw InitAPIMoreThanOnce(this::class)
        }

        // A LinearOpMode must be passed into this function if `isLinear` is true.
        if (this.isLinear && opMode !is LinearOpMode) {
            throw InitLinearAPIWithoutLinearOpMode(this::class)
        }

        this.nullableOpMode = opMode

        Dependencies.registerAPI(this)

        log.debug("Initialized API ${this::class.simpleName}.")
    }

    companion object {
        private val log = Logging.logger(this)
    }
}
