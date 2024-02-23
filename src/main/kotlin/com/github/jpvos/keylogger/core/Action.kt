package com.github.jpvos.keylogger.core

data class Action(
    val type: Type,
    val name: String
) {
    enum class Type {
        Mouse,
        Keystroke,
        EditorAction;

        companion object {
            fun parse(type: String): Type {
                return when (type) {
                    "Mouse" -> Mouse
                    "Keystroke" -> Keystroke
                    "EditorAction" -> EditorAction
                    else -> throw IllegalArgumentException("Unknown action type: $type")
                }
            }
        }

        override fun toString(): String {
            return when (this) {
                Mouse -> "Mouse"
                Keystroke -> "Keystroke"
                EditorAction -> "EditorAction"
            }
        }
    }
}
