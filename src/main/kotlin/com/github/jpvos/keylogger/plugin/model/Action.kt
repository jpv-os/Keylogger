package com.github.jpvos.keylogger.plugin.model

/**
 * An action that the user has performed.
 */
data class Action(
    /**
     * The type of action.
     */
    val type: Type,
    /**
     * The name of the action.
     */
    val name: String
) {
    /**
     * The type of action.
     */
    enum class Type {
        /**
         * A mouse action.
         */
        Mouse,

        /**
         * A keystroke action.
         */
        Keystroke,

        /**
         * An editor action.
         */
        EditorAction;

        companion object {
            /**
             * The string representation of the type of action.
             */
            private const val TYPE_MOUSE = "Mouse"

            /**
             * The string representation of the type of action.
             */
            private const val TYPE_KEYSTROKE = "Keystroke"

            /**
             * The string representation of the type of action.
             */
            private const val TYPE_EDITOR_ACTION = "EditorAction"

            /**
             * Parse the type of action from a string.
             */
            fun parse(type: String): Type {
                return when (type) {
                    TYPE_MOUSE -> Mouse
                    TYPE_KEYSTROKE -> Keystroke
                    TYPE_EDITOR_ACTION -> EditorAction
                    else -> throw IllegalArgumentException("Unknown action type: $type")
                }
            }
        }

        /**
         * The string representation of the type of action.
         */
        override fun toString(): String {
            return when (this) {
                Mouse -> TYPE_MOUSE
                Keystroke -> TYPE_KEYSTROKE
                EditorAction -> TYPE_EDITOR_ACTION
            }
        }
    }
}
