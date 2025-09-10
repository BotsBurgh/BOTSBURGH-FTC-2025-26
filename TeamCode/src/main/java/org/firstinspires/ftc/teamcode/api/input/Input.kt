package org.firstinspires.ftc.teamcode.api.input

import org.firstinspires.ftc.teamcode.core.API
import org.firstinspires.ftc.teamcode.core.Resettable

object Input : API() {
    val gamepad1: GamepadEx by Resettable { GamepadEx(GamepadEx.GamepadNumber.Gamepad1) }
    val gamepad2: GamepadEx by Resettable { GamepadEx(GamepadEx.GamepadNumber.Gamepad2) }

    /**
     * Returns true if [button] is currently pressed for [gamepad1].
     *
     * @see update
     */
    fun pressed(button: Button) = this.gamepad1.pressed(button)

    /**
     * Returns true if [button] was pressed this update for [gamepad1].
     *
     * @see update
     */
    fun justPressed(button: Button) = this.gamepad1.justPressed(button)

    /**
     * Returns true if [button] was released this update for [gamepad1].
     *
     * @see update
     */
    fun justReleased(button: Button) = this.gamepad1.justReleased(button)

    /**
     * Calls [GamepadEx.update] for both [gamepad1] and [gamepad2].
     */
    fun update() {
        this.gamepad1.update(this.opMode)
        this.gamepad2.update(this.opMode)
    }
}
