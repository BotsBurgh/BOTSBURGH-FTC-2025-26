package org.firstinspires.ftc.teamcode.core

import kotlin.reflect.KProperty

/**
 * A global set of functions that need to be called between opmodes being run.
 *
 * These functions should reset all state so that it does not persist.
 */
private val resetFunctions = mutableSetOf<() -> Unit>()

/**
 * A property delegate that instruments resetting it between opmode runs.
 *
 * This is used in [delegate properties](https://kotlinlang.org/docs/delegated-properties.html). You
 * must pass a function when constructing the resettable property that returns its default value.
 *
 * # Example
 *
 * ```
 * object MyAPI : API() {
 *     // The starting value of someState is 10.
 *     private var someState: Int by Resettable { 10 }
 *
 *     fun mutateState() {
 *         // `Resettable` variables can be mutated as normal.
 *         someState = 15
 *         assert(someState == 15)
 *     }
 * }
 * ```
 */
class Resettable<T>(private val default: () -> T) {
    // This is the actual value.
    private var inner: T = this.default()

    init {
        // Adds the reset function to the global set.
        resetFunctions.add(this::reset)
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = this.inner

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        this.inner = value
    }

    private fun reset() {
        this.inner = this.default()
    }

    companion object {
        internal fun resetAll() {
            // Call each function in the global set.
            resetFunctions.forEach { it() }
        }
    }
}
