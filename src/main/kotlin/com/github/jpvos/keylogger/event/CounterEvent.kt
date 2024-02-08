package com.github.jpvos.keylogger.event

data class CounterEvent(
    val actions: Map<ActionEvent, Long>,
    val totalActiveTime: Long,
    val totalIdleTime: Long
)
