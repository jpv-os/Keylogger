package com.github.jpvos.keylogger.event


data class ActionEvent(
    val type: Type,
    val name: String
) {
    enum class Type {
        Mouse,
        Keystroke,
        EditorAction
    }
}
