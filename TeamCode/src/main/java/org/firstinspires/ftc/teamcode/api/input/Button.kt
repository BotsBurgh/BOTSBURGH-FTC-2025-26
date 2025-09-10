package org.firstinspires.ftc.teamcode.api.input

import com.qualcomm.robotcore.hardware.Gamepad

/** Represents a button on a [Gamepad]. */
enum class Button {
    DPadUp {
        override fun pressed(gamepad: Gamepad) = gamepad.dpad_up
    },
    DPadDown {
        override fun pressed(gamepad: Gamepad) = gamepad.dpad_down
    },
    DPadLeft {
        override fun pressed(gamepad: Gamepad) = gamepad.dpad_left
    },
    DPadRight {
        override fun pressed(gamepad: Gamepad) = gamepad.dpad_right
    },
    A {
        override fun pressed(gamepad: Gamepad) = gamepad.a
    },
    B {
        override fun pressed(gamepad: Gamepad) = gamepad.b
    },
    X {
        override fun pressed(gamepad: Gamepad) = gamepad.x
    },
    Y {
        override fun pressed(gamepad: Gamepad) = gamepad.y
    },
    Start {
        override fun pressed(gamepad: Gamepad) = gamepad.start
    },
    Back {
        override fun pressed(gamepad: Gamepad) = gamepad.back
    },
    LeftBumper {
        override fun pressed(gamepad: Gamepad) = gamepad.left_bumper
    },
    RightBumper {
        override fun pressed(gamepad: Gamepad) = gamepad.right_bumper
    },
    LeftStickButton {
        override fun pressed(gamepad: Gamepad) = gamepad.left_stick_button
    },
    RightStickButton {
        override fun pressed(gamepad: Gamepad) = gamepad.right_stick_button
    },
    ;

    abstract fun pressed(gamepad: Gamepad): Boolean
}
