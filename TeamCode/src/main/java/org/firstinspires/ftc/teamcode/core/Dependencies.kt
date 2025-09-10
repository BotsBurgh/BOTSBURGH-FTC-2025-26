package org.firstinspires.ftc.teamcode.core

import org.firstinspires.ftc.teamcode.core.logging.Logging

/**
 * A listener that ensures API dependencies are properly initialized when the start button is
 * pressed.
 */
object Dependencies {
    internal val initializedAPIs: MutableSet<API> by Resettable { mutableSetOf() }
    private val log = Logging.logger(this)

    /**
     * Registers an API as initialized.
     *
     * This is called within the super implementation of [API.init], and should never be called
     * manually.
     */
    internal fun registerAPI(api: API) {
        log.debug("Registered API ${api::class.simpleName}.")
        this.initializedAPIs.add(api)
    }

    fun checkDependencies() {
        log.debug("Checking API dependencies.")

        for (api in this.initializedAPIs) {
            for (dep in api.dependencies) {
                if (!this.initializedAPIs.contains(dep)) {
                    throw MissingDependency(api::class, dep::class, this.initializedAPIs)
                }
            }
        }
    }
}
