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

    private fun percent(value: Double): String {
        require(value in 0.0..100.0)
        return "${"%.4f".format(value)}%"
    }

    fun percent01(value: Double): String {
        require(value in 0.0..1.0)
        return percent(value * 100.0)
    }

    fun duration(value: Long): String {
        val years = value / (365L * 24 * 60 * 60 * 1000)
        val months = (value % (365L * 24 * 60 * 60 * 1000)) / (30L * 24 * 60 * 60 * 1000)
        val weeks = (value % (30L * 24 * 60 * 60 * 1000)) / (7 * 24 * 60 * 60 * 1000)
        val days = (value % (7 * 24 * 60 * 60 * 1000)) / (24 * 60 * 60 * 1000)
        val hours = (value % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
        val minutes = (value % (60 * 60 * 1000)) / (60 * 1000)
        val seconds = (value % (60 * 1000)) / 1000
        val milliseconds = value % 1000
        val strBuilder = mutableListOf<String>()
        strBuilder.add("${milliseconds}ms")
        if (seconds > 0) strBuilder.add("${seconds}s")
        if (minutes > 0) strBuilder.add("${minutes}m")
        if (hours > 0) strBuilder.add("${hours}h")
        if (days > 0) strBuilder.add("${days}d")
        if (weeks > 0) strBuilder.add("${weeks}w")
        if (months > 0) strBuilder.add("${months}m")
        if (years > 0) strBuilder.add("${years}y")
        return strBuilder.reversed().joinToString(" ")
    }
}
