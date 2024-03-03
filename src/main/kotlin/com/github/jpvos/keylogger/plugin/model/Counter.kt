package com.github.jpvos.keylogger.plugin.model


class Counter(
    private var idleTimeout: Long = 1000L,
    private var actions: MutableMap<Action, Long> = mutableMapOf(),
    private var activeTime: Long = 0L,
    private var idleTime: Long = 0L,
    private var lastActionTime: Long? = null,
    private var lastUpdateTime: Long = System.currentTimeMillis(),
) {

    data class State(
        val actions: Map<Action, Long>,
        val activeTime: Long,
        val idleTime: Long
    ) {
        val totalTime by lazy {
            activeTime + idleTime
        }
        val actionsPerMinute by lazy {
            if (activeTime == 0L) {
                0.0
            } else {
                val minutes = activeTime / 60000.0
                totalActions / minutes
            }
        }
        val uniqueActions by lazy {
            actions.size.toLong()
        }
        val totalActions by lazy {
            actions.toList().sumOf { it.second }
        }
    }

    interface Listener {
        fun onAction(action: Action)
    }

    private val listeners = mutableListOf<Listener>()

    fun setState(state: State) {
        actions.clear()
        actions.putAll(state.actions)
        activeTime = state.activeTime
        idleTime = state.idleTime
        lastActionTime = null
        lastUpdateTime = System.currentTimeMillis()
    }

    fun getState(): State {
        accumulateTime()
        return State(actions, activeTime, idleTime)
    }

    fun next(action: Action) {
        accumulateTime()
        lastActionTime = System.currentTimeMillis()
        actions[action] = (actions[action] ?: 0L) + 1L
        accumulateTime()
        listeners.forEach { it.onAction(action) }
    }

    fun setIdleTimeout(idleTimeout: Long) {
        this.idleTimeout = idleTimeout
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun accumulateTime() {
        val now = System.currentTimeMillis()
        val timeSinceLastUpdate = now - lastUpdateTime
        val last = lastActionTime
        val isActive = last != null && (now - last) < idleTimeout
        if (isActive) {
            activeTime += timeSinceLastUpdate
        } else {
            idleTime += timeSinceLastUpdate
        }
        lastUpdateTime = now
    }
}
