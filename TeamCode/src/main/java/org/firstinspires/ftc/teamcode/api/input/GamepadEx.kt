package org.firstinspires.ftc.teamcode.api.input

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad

/**
 * An advanced version of [Gamepad] that detects immediate presses and releases, useful for advanced
 * controls and user-interfaces.
 *
 * This needs to be updatedd repeatedly by calling [update] to detect new input.
 *
 * Usually you do not want to create this yourself. Instead, go through the [Input] API.
 */
class GamepadEx(private val number: GamepadNumber) {
    /** Represents which of the 2 possible gamepads this [GamepadEx] is associated with. */
    enum class GamepadNumber {
        Gamepad1,
        Gamepad2,
    }

    // This is where the main state is kept. We use a set here because is de-duplicates values.
    // (B cannot be pressed twice at the same time!)
    private val pressed: MutableSet<Button> = mutableSetOf()
    private val justPressed: MutableSet<Button> = mutableSetOf()
    private val justReleased: MutableSet<Button> = mutableSetOf()

    /**
     * Returns true if [button] is currently pressed.
     *
     * @see update
     */
    fun pressed(button: Button): Boolean = button in this.pressed

    /**
     * Returns true if [button] was pressed this update.
     *
     * @see update
     */
    fun justPressed(button: Button): Boolean = button in this.justPressed

    /**
     * Returns true if [button] was released this update.
     *
     * @see update
     */
    fun justReleased(button: Button): Boolean = button in this.justReleased

    /**
     * Updates this for new gamepad events.
     *
     * You likely want to call this within a loop to react to new button presses and releases.
     *
     * @see OpMode.loop
     * @see OpMode.init_loop
     * @see LinearOpMode.opModeIsActive
     * @see LinearOpMode.opModeInInit
     */
    fun update(opMode: OpMode) {
        this.justPressed.clear()
        this.justReleased.clear()

        val gamepad =
            when (this.number) {
                GamepadNumber.Gamepad1 -> opMode.gamepad1
                GamepadNumber.Gamepad2 -> opMode.gamepad2
            }

        for (button in Button.entries) {
            if (button.pressed(gamepad)) {
                // This button is pressed, so add it to the set. `add()` will return true if the
                // button did not exist, in which case we also want to add it to `justPressed`.
                if (this.pressed.add(button)) {
                    this.justPressed.add(button)
                }
            } else {
                // This button is not pressed, so we remove it from the set. `remove()` returns true
                // if the button was found to be removed, in which case we add it to `justReleased`.
                if (this.pressed.remove(button)) {
                    this.justReleased.add(button)
                }
            }
        }
    }
}
