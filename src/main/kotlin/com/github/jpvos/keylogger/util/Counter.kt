package com.github.jpvos.keylogger.util

data class Counter<T>(
    private var actions: MutableMap<T, Long> = mutableMapOf(),
    private var totalActiveTime: Long = 0L,
    private var totalIdleTime: Long = 0L,
    private var lastActionTime: Long? = null,
    private var lastUpdateTime: Long = System.currentTimeMillis()
) {
    fun getActions(): Map<T, Long> = actions
    fun getTotalActiveTime(): Long = totalActiveTime
    fun getTotalIdleTime(): Long = totalIdleTime

    fun next(action: T) {
        accumulateTime()
        lastActionTime = System.currentTimeMillis()
        actions[action] = (actions[action] ?: 0L) + 1L
        accumulateTime()
    }

    private fun accumulateTime() {
        val now = System.currentTimeMillis()
        val timeSinceLastUpdate = now - lastUpdateTime
        val last = lastActionTime
        val idleTimeout = 1000L // TODO: configurable
        val isActive = last != null && (now - last) < idleTimeout
        if (isActive) {
            totalActiveTime += timeSinceLastUpdate
        } else {
            totalIdleTime += timeSinceLastUpdate
        }
        lastUpdateTime = now
    }
}
