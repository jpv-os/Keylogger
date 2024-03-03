package com.github.jpvos.keylogger.plugin.util.components

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

sealed class TableCell {
    data class Label(val value: String) : TableCell() {
        override fun toString(): String {
            return value
        }
    }

    data class Long(val value: kotlin.Long) : TableCell() {
        override fun toString(): String {
            return "%,d".format(value)
        }
    }

    data class Date(val value: kotlin.Long) : TableCell() {
        override fun toString(): String {
            return DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss:SSS (z)")
                .format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault()))
        }
    }

    data class Decimal(val value: Double) : TableCell() {
        override fun toString(): String {
            return "%.2f".format(value)
        }
    }

    data class Percent(val value: Double) : TableCell() {
        init {
            require(value in 0.0..100.0)
        }

        override fun toString(): String {
            return "${"%.4f".format(value)}%"
        }
    }

    data class Duration(val value: kotlin.Long) : TableCell() {
        override fun toString(): String {
            val years = value / (365L * 24 * 60 * 60 * 1000)
            val months = (value % (365L * 24 * 60 * 60 * 1000)) / (30L * 24 * 60 * 60 * 1000)
            val weeks = (value % (30L * 24 * 60 * 60 * 1000)) / (7 * 24 * 60 * 60 * 1000)
            val days = (value % (7 * 24 * 60 * 60 * 1000)) / (24 * 60 * 60 * 1000)
            val hours = (value % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
            val minutes = (value % (60 * 60 * 1000)) / (60 * 1000)
            val seconds = (value % (60 * 1000)) / 1000
            val milliseconds = value % 1000
            val strBuilder = mutableListOf<String>()
            strBuilder.add("${milliseconds.toString().padStart(3, '0')}ms")
            if (seconds > 0) strBuilder.add("${seconds.toString().padStart(2, '0')}s")
            if (minutes > 0) strBuilder.add("${minutes.toString().padStart(2, '0')}m")
            if (hours > 0) strBuilder.add("${hours.toString().padStart(2, '0')}h")
            if (days > 0) strBuilder.add("${days.toString().padStart(2, '0')}d")
            if (weeks > 0) strBuilder.add("${weeks.toString().padStart(2, '0')}w")
            if (months > 0) strBuilder.add("${months.toString().padStart(2, '0')}m")
            if (years > 0) strBuilder.add("${years}y")
            return strBuilder.reversed().joinToString(" ")
        }
    }
}
