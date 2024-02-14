package com.github.jpvos.keylogger.ui

object DisplayFormat {
    fun text(value: String): String {
        return value
    }
    fun long(value: Long): String {
        return "%,d".format(value)
    }
    fun milliseconds(value: Long): String {
        return "${long(value)} ms"
    }

    fun decimal(value: Double): String {
        return "${"%.2f".format(value)} apm"
    }
}
