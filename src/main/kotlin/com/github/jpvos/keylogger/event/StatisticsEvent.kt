package com.github.jpvos.keylogger.event

data class StatisticsEvent(
    val actions: Map<ActionEvent, Long>,
    val totalActiveTime: Long,
    val totalIdleTime: Long,
) {
    val totalActions by lazy {
        actions.values.sum()
    }

    val uniqueActions by lazy {
        actions.size
    }

    val totalTime by lazy {
        totalActiveTime + totalIdleTime
    }

    val apm by lazy {
        if (totalActiveTime == 0L) 0.0
        else totalActions.toDouble() / totalActiveTime * 1000 * 60
    }
}
