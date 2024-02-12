package com.github.jpvos.keylogger.core

data class Action(
    val type: Type,
    val name: String
) {
    enum class Type {
        Mouse,
        Keystroke,
        EditorAction
    }
}
