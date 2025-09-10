package org.firstinspires.ftc.teamcode.core

import kotlin.reflect.KClass

class InitAPIMoreThanOnce(clazz: KClass<out API>) : IllegalStateException("Tried to initialize API ${clazz.simpleName} more than once.")

class InitLinearAPIWithoutLinearOpMode(clazz: KClass<out API>) : IllegalArgumentException(
    """
    Tried to initialize linear API ${clazz.simpleName} without a `LinearOpMode`.
    Please make sure that your opmode extends `LinearOpMode`.
    Also, please check ${clazz.simpleName} has not accidentally set `isLinear` to true.
    """.trimIndent(),
)

class APINotInitialized(clazz: KClass<out API>) : NullPointerException(
    """
    API ${clazz.simpleName} has not been initialized before being used.
    Please ensure that you call `${clazz.simpleName}.init(this)` within the opmode, and that ${clazz.simpleName} calls `super.init(opMode)` within its definition.
    """.trimIndent(),
)

class IllegalLinearOpModeAccess(clazz: KClass<out API>) : RuntimeException(
    """
    API ${clazz.simpleName} tried to access `linearOpMode` without setting `isLinear` to true.
    Please add `override val isLinear = true` to the API definition.
    """.trimIndent(),
)

class MissingDependency(source: KClass<out API>, dependency: KClass<out API>, initializedAPIs: Set<API>) : RuntimeException(
    """
    API ${source.simpleName} requires dependency ${dependency.simpleName}, but it was not initialized.
    Please call `${dependency.simpleName}.init(this)` in the opmode.
    If ${source.simpleName} does not depend on ${dependency.simpleName}, remove it from `${source.simpleName}.dependencies`.
    Initialized APIs: $initializedAPIs.
    """.trimIndent(),
)
