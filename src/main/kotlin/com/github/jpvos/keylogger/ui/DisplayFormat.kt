package com.github.jpvos.keylogger.ui

object DisplayFormat {
    fun text(value: String): String {
        return value
    }
    fun long(value: Long): String {
        return "%,d".format(value)
    }
    fun milliseconds(value: Long): String {
        return "${long(value)}ms"
    }

    fun decimal(value: Double): String {
        return "%.2f".format(value)
    }

    fun percent(value: Double): String {
        require(value in 0.0..100.0)
        return "${decimal(value)}%"
    }

    fun percent01(value: Double): String {
        require(value in 0.0..1.0)
        return percent(value * 100.0)
    }
}
